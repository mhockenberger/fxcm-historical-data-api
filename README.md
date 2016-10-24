# FXCM Historical Data API

[![GitHub issues](https://img.shields.io/github/issues/mhockenberger/fxcm-historical-data-api.svg)](https://github.com/mhockenberger/fxcm-historical-data-api/issues) [![GitHub forks](https://img.shields.io/github/forks/mhockenberger/fxcm-historical-data-api.svg)](https://github.com/mhockenberger/fxcm-historical-data-api/network) [![GitHub stars](https://img.shields.io/github/stars/mhockenberger/fxcm-historical-data-api.svg)](https://github.com/mhockenberger/fxcm-historical-data-api/stargazers)

## Description

**FXCM Historical Data API** is an API designed to access historical prices of financial instruments directly from FXCM.

The following pricing data is available:

* 39 FX pairs including majors and exotics
* CFDs including equities, metals & oil
* 1 / 5 / 15 / 30 min(s), 1 / 4 hour(s), 1 day, 1 week and 1 month data
* Bid / Ask data

## Features

* Login / Logout
* Historical prices of financial instruments request for specified timeframe of the predefined period or last trading day
* If the request is not filled completely, additional request(s) will be sent until completion
* Storage of gathered information

## Arguments

* `/login | --login | /l | -l` - Your username.
* `/password | --password | /p | -p` - Your password.
* `/url | --url | /u | -u` - The server URL. For example, [http://www.fxcorporate.com/Hosts.jsp](http://www.fxcorporate.com/Hosts.jsp). 
* `/connection | --connection | /c | -c` - The connection name. For example, *"Demo"* or *"Real"*.
* `/sessionid | --sessionid` - The database name. Required only for users who have accounts in more than one database. Optional parameter.
* `/pin | --pin` - Your pin code. Required only for users who have a pin. Optional parameter.
* `/instrument | --instrument | /i | -i` - An instrument which you want to use in sample. For example, *"GER30"* or *"EUR/USD"*.
* `/timeframe | --timeframe` - Time period which forms a single candle. For example, *"t1"* - for 1 tick, *"m1"* - for 1 minute or *"H1"* - for 1 hour.
* `/datefrom | --datefrom` - DateTime from which you want to receive historical prices. If you leave this argument as it is, it will mean *datefrom* last trading day. Format is *"MM.dd.yyyy HH:mm:ss"*. Optional parameter.
* `/dateto | --dateto` - DateTime until which you want to receive historical prices. If you leave this argument as it is, it will mean *dateto* now. Format is *"MM.dd.yyyy HH:mm:ss"*. Optional parameter.
* `/outputdir | --outputdir | /o | -o` - The destination folder for downloaded data.

## Run configurations

```
Main class: 				de.mhockenberger.fxcmhdapi.core.HistData
VM options: 				-Djava.library.path=libs:libs/java
Program arguments:			-l "{Username}"
							-p "{Password}"
							-u "{URL}"
							-c "{Connection}" 
							-i "{Instrument}"
							--timeframe "{Timeframe}"
							-o "{Output directory}"
Working directory:			./IdeaProjects/FXCM/
Environment variables:
Use classpath of module:	FXCM
JRE:						1.8
```

### JAR-Packaged HistData

```
sudo java -Djava.library.path=libs:libs/java -jar artifacts/HistData_jar/HistData.jar
```

## Dev / Requirements / Used SDKs

* [OS X](http://www.apple.com/de/downloads/)
* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Intellij IDEA](https://www.jetbrains.com/idea/)
* [FXCM *Standard* account](https://www.fxcm.com/de/forex-konto-eroeffnung/)
* [ForexConnectAPI 1.4.1 (*Darwin-universal*)](http://fxcodebase.com/wiki/index.php/Download#Beta_Release_.281.4.1.29)

## Risk disclaimer

The API does not take into consideration your trading objectives, financial situation, needs and level of experience. Therefore it should not be considered as a personal recommendation or investment advice. 

Please seek advice from a separate financial advisor.