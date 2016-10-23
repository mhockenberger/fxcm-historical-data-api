package de.mhockenberger.fxcmhdapi.core;

public class Arguments {
    public String getArgument(String[] args, String sKey) {
        for (int i = 0; i < args.length; i++) {
            int iDelimOffset = 0;

            if (args[i].startsWith("--")) {
                iDelimOffset = 2;
            } else if (args[i].startsWith("-") || args[i].startsWith("/")) {
                iDelimOffset = 1;
            }

            if (args[i].substring(iDelimOffset).equals(sKey) && (args.length > i+1)) {
                return args[i+1];
            }
        }

        return "";
    }
}
