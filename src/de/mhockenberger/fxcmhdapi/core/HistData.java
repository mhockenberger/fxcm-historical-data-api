package de.mhockenberger.fxcmhdapi.core;

import java.text.*;
import java.util.*;
import java.io.*;
import com.fxcore2.*;

public class HistData {
    static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
    
    public static void main(String[] args) {
        O2GSession session = null;

        try {
            String sProcName = "FXCM Historical Data API";

            if (args.length == 0) {
                printHelp(sProcName);

                return;
            }

            LoginParams loginParams = new LoginParams(args);
            SampleParams sampleParams = new SampleParams(args);
            LocalParams localParams = new LocalParams(args);

            printSampleParams(sProcName, loginParams, sampleParams, localParams);
            checkObligatoryParams(loginParams, sampleParams, localParams);

            session = O2GTransport.createSession();
            SessionStatusListener statusListener = new SessionStatusListener(session, loginParams.getSessionID(), loginParams.getPin());
            session.subscribeSessionStatus(statusListener);
            statusListener.reset();
            session.login(loginParams.getLogin(), loginParams.getPassword(), loginParams.getURL(), loginParams.getConnection());

            if (statusListener.waitEvents() && statusListener.isConnected()) {
                ResponseListener responseListener = new ResponseListener();
                session.subscribeResponse(responseListener);
                getHistoryPrices(session, sampleParams.getInstrument(), sampleParams.getTimeframe(), sampleParams.getDateFrom(), sampleParams.getDateTo(), localParams.getOutputDir(), responseListener);

                System.out.println("Done!");

                statusListener.reset();
                session.logout();
                statusListener.waitEvents();
                session.unsubscribeResponse(responseListener);
            }

            session.unsubscribeSessionStatus(statusListener);
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
    }

    public static void getHistoryPrices(O2GSession session, String sInstrument, String sTimeframe, Calendar dtFrom, Calendar dtTo, String sOutputDir, ResponseListener responseListener) throws Exception {
        O2GRequestFactory factory = session.getRequestFactory();

        O2GTimeframe timeframe = factory.getTimeFrameCollection().get(sTimeframe);

        if (timeframe == null) {
            throw new Exception(String.format("Timeframe '%s' is incorrect!", sTimeframe));
        }

        O2GRequest request = factory.createMarketDataSnapshotRequestInstrument(sInstrument, timeframe, 300);

        if (request == null)
        {
            throw new Exception("Could not create request.");
        }

        Calendar dtFirst = dtTo;
        Calendar dtEarliest;

        if (dtFrom == null) {
            dtEarliest = Calendar.getInstance();
            dtEarliest.setTime(new Date(Long.MIN_VALUE));
        } else {
            dtEarliest = dtFrom;
        }

        PrintWriter pw = new PrintWriter(new File(String.format("%s/%s",
                sOutputDir, String.format("%s_%s_%s.txt", mDateFormat.format(new Date()), sInstrument, sTimeframe).toLowerCase().replaceAll("\\W", "_"))));
        if (pw == null)
        {
            throw new Exception("Could not create file.");
        }

        do {
            factory.fillMarketDataSnapshotRequestTime(request, dtFrom, dtFirst, false, O2GCandleOpenPriceMode.PREVIOUS_CLOSE);
            responseListener.setRequestID(request.getRequestId());
            session.sendRequest(request);

            if (!responseListener.waitEvents()) { }

            O2GResponse response = responseListener.getResponse();
            if (response != null && response.getType() == O2GResponseType.MARKET_DATA_SNAPSHOT) {
                O2GResponseReaderFactory readerFactory = session.getResponseReaderFactory();

                if (readerFactory != null) {
                    O2GMarketDataSnapshotResponseReader reader = readerFactory.createMarketDataSnapshotReader(response);

                    if (reader.size() > 0) {
                        if (dtFirst == null || dtFirst.compareTo(reader.getDate(0)) != 0) {
                            dtFirst = reader.getDate(0);
                        } else {
                            break;
                        }
                    } else {
                        System.out.println("0 rows received");

                        break;
                    }
                }

                pw.write(storePrices(session, response, sInstrument.toLowerCase(), sTimeframe.toLowerCase()).toString());
                pw.flush();
            } else {
                break;
            }
        } while (dtFirst.after(dtEarliest));

        pw.close();
    }

    public static StringBuilder storePrices(O2GSession session, O2GResponse response, String instrument, String timeframe) {
        System.out.println(String.format("Request with RequestID=%s is completed.", response.getRequestId()));

        StringBuilder sb = new StringBuilder();

        O2GResponseReaderFactory factory = session.getResponseReaderFactory();
        if (factory != null) {
            O2GMarketDataSnapshotResponseReader reader = factory.createMarketDataSnapshotReader(response);

            for (int i = reader.size() - 1; i >= 0; i--) {
                if (reader.isBar()) {
                    sb.append(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n",
                            instrument, timeframe,
                            mDateFormat.format(reader.getDate(i).getTime()), reader.getBidOpen(i), reader.getBidHigh(i), reader.getBidLow(i), reader.getBidClose(i),
                            reader.getAskOpen(i), reader.getAskHigh(i), reader.getAskLow(i), reader.getAskClose(i), reader.getVolume(i)));
                } else {
                    sb.append(String.format("%s;%s;%s;%s;%s;%s\n", instrument, timeframe, mDateFormat.format(reader.getDate(i).getTime()), reader.getBidClose(i), reader.getAskClose(i), reader.getVolume(i)));
                }
            }
        }

        return sb;
    }
    
    private static void printHelp(String sProcName)
    {
        System.out.println(sProcName + " sample parameters:\n");
        
        System.out.println("/login | --login | /l | -l");
        System.out.println("Your username.\n");
        
        System.out.println("/password | --password | /p | -p");
        System.out.println("Your password.\n");
        
        System.out.println("/url | --url | /u | -u");
        System.out.println("The server URL. For example, http://www.fxcorporate.com/Hosts.jsp.\n");
        
        System.out.println("/connection | --connection | /c | -c");
        System.out.println("The connection name. For example, \"Demo\" or \"Real\".\n");
        
        System.out.println("/sessionid | --sessionid");
        System.out.println("The database name. Required only for users who have accounts in more than one database. Optional parameter.\n");
        
        System.out.println("/pin | --pin");
        System.out.println("Your pin code. Required only for users who have a pin. Optional parameter.\n");
        
        System.out.println("/instrument | --instrument | /i | -i");
        System.out.println("An instrument which you want to use in sample. For example, \"GER30\" or \"EUR/USD\".\n");
        
        System.out.println("/timeframe | --timeframe");
        System.out.println("Time period which forms a single candle. For example, \"t1\" - for 1 tick, \"m1\" - for 1 minute or \"H1\" - for 1 hour.\n");
        
        System.out.println("/datefrom | --datefrom");
        System.out.println("DateTime from which you want to receive historical prices. If you leave this argument as it is, it will mean from last trading day. Format is \"MM.dd.yyyy HH:mm:ss\". Optional parameter.\n");
        
        System.out.println("/dateto | --dateto");
        System.out.println("DateTime until which you want to receive historical prices. If you leave this argument as it is, it will mean to now. Format is \"MM.dd.yyyy HH:mm:ss\". Optional parameter.\n");

        System.out.println("/outputdir | --outputdir | /o | -o");
        System.out.println("The destination folder for downloaded data.");
    }

    private static void checkObligatoryParams(LoginParams loginParams, SampleParams sampleParams, LocalParams localParams) throws Exception {
        if(loginParams.getLogin().isEmpty()) {
            throw new Exception(LoginParams.LOGIN_NOT_SPECIFIED);
        }

        if(loginParams.getPassword().isEmpty()) {
            throw new Exception(LoginParams.PASSWORD_NOT_SPECIFIED);
        }

        if(loginParams.getURL().isEmpty()) {
            throw new Exception(LoginParams.URL_NOT_SPECIFIED);
        }

        if(loginParams.getConnection().isEmpty()) {
            throw new Exception(LoginParams.CONNECTION_NOT_SPECIFIED);
        }

        if(sampleParams.getInstrument().isEmpty()) {
            throw new Exception(SampleParams.INSTRUMENT_NOT_SPECIFIED);
        }

        if(sampleParams.getTimeframe().isEmpty()) {
            throw new Exception(SampleParams.TIMEFRAME_NOT_SPECIFIED);
        }

        boolean bIsDateFromNotSpecified = false;
        boolean bIsDateToNotSpecified = false;
        Calendar dtFrom = sampleParams.getDateFrom();
        Calendar dtTo = sampleParams.getDateTo();

        if (dtFrom == null) {
            bIsDateFromNotSpecified = true;
        } else {
            if(!dtFrom.before(Calendar.getInstance(TimeZone.getTimeZone("UTC")))) {
                throw new Exception(String.format("\"DateFrom\" value %s is invalid", dtFrom));
            }
        }

        if (dtTo == null) {
            bIsDateToNotSpecified = true;
        } else {
            if(!bIsDateFromNotSpecified && !dtFrom.before(dtTo)) {
                throw new Exception(String.format("\"DateTo\" value %s is invalid", dtTo));
            }
        }

        if (localParams.getOutputDir().isEmpty()) {
            throw new Exception(LocalParams.OUTPUTDIR_NOT_SPECIFIED);
        }
    }

    private static void printSampleParams(String procName, LoginParams loginPrm, SampleParams samplePrm, LocalParams localPrm) {
        System.out.println(String.format("Running %s with arguments:", procName));

        if (loginPrm != null) {
            System.out.println(String.format("%s * %s %s %s %s", loginPrm.getLogin(), loginPrm.getURL(),
                    loginPrm.getConnection(), loginPrm.getSessionID(), loginPrm.getPin()));
        }

        if (samplePrm != null) {
            System.out.println(String.format("Instrument='%s', Timeframe='%s', DateFrom='%s', DateTo='%s'",
                    samplePrm.getInstrument(), samplePrm.getTimeframe(),
                    samplePrm.getDateFrom() == null ? "" : mDateFormat.format(samplePrm.getDateFrom().getTime()),
                    samplePrm.getDateTo() == null ? "" : mDateFormat.format(samplePrm.getDateTo().getTime())));
        }

        if (localPrm != null) {
            System.out.println(String.format("Output directory='%s'", localPrm.getOutputDir()));
        }
    }
}
