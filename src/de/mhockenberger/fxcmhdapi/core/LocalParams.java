package de.mhockenberger.fxcmhdapi.core;

public class LocalParams extends Arguments {
    public static final String OUTPUTDIR_NOT_SPECIFIED = "'Output directory' is not specified (/outputdir | --outputdir | /o | -o)";

    public String getOutputDir() {
        return mOutputDir;
    }
    private String mOutputDir;

    public LocalParams(String[] args) {
        mOutputDir = getArgument(args, "o");

        if (mOutputDir.isEmpty())
            mOutputDir = getArgument(args, "outputdir");
    }
}
