package com.digiwallet.utils;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.digiwallet.exception.WalletException;

/**
 *
 * @author Lokesh
 */
public class WalletLogger 
{
    final Logger oLogger;
    
    public WalletLogger()
    {
        oLogger = Logger.getLogger(WalletLogger.class.getName());
//        oLogger.setLevel(Level.ERROR);
    }
    
    public WalletLogger(String strClassName)
    {
        oLogger = Logger.getLogger(strClassName);
//        oLogger.setLevel(getLogLevelFromDB());
    }
    
    public WalletLogger(Class classClass)
    {
        oLogger = Logger.getLogger(classClass);
//        oLogger.setLevel(getLogLevelFromDB());
    }
    
    public final void logEntering(String METHOD_NAME)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT)
        oLogger.debug(METHOD_NAME + ": ENTRY");
    }
    
    public final void logEntering(String METHOD_NAME, String strArguments)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT){
        oLogger.debug(METHOD_NAME + ": ENTRY");
        oLogger.debug(METHOD_NAME + ": ARGUMENTS (" + strArguments + ")");
        }
    }

    public final void logExiting(String METHOD_NAME)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT)
        oLogger.debug(METHOD_NAME + ": EXIT");
    }

    public final void logExiting(String METHOD_NAME, String strReturnValue)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT){
        oLogger.debug(METHOD_NAME + ": RETURN VALUE : (" + strReturnValue + ")");
        oLogger.debug(METHOD_NAME + ": EXIT");
        }
    }

    public final void logDebug(String METHOD_NAME, String message)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT)
        oLogger.debug(METHOD_NAME + ": " + message);
    }

    public final void logInfo(String METHOD_NAME, String message)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.INFO_INT)
        oLogger.info(METHOD_NAME + ": " + message);
    }

    public final void logFatal(String METHOD_NAME, String message)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.FATAL_INT)
        oLogger.fatal(METHOD_NAME + ": " + message);
    }

    public final void logFatal(String METHOD_NAME, String message, Exception ex)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT) oLogger.debug(METHOD_NAME + ": Cause : " + this.getStackTrace(ex));
        if(getLogLevelFromDB() <= Level.FATAL_INT) oLogger.fatal(METHOD_NAME + ": " + message + ": " + ex + ": " + ex.getMessage());
    }

    public final void logFatal(String METHOD_NAME, Exception ex)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT) oLogger.debug(METHOD_NAME + ": Cause : " + this.getStackTrace(ex));
        if(getLogLevelFromDB() <= Level.FATAL_INT) oLogger.fatal(METHOD_NAME + ": " + ex + ": " + ex.getMessage());
    }

    public final void logError(String METHOD_NAME, String message)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.ERROR_INT)
        oLogger.error(METHOD_NAME + ": " + message);
    }

    public final void logError(String METHOD_NAME, String message, Exception ex)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT) oLogger.debug(METHOD_NAME + ": Cause : " + this.getStackTrace(ex));
        if(getLogLevelFromDB() <= Level.ERROR_INT) oLogger.error(METHOD_NAME + ": " + message + ": " + ex + ": " + ex.getMessage());
    }

    public final void logError(String METHOD_NAME, Exception ex)
    {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT) oLogger.debug(METHOD_NAME + ": Cause : " + this.getStackTrace(ex));
        if(getLogLevelFromDB() <= Level.ERROR_INT) oLogger.error(METHOD_NAME + ": " + ex + ": " + ex.getMessage());
    }
    
    private String getStackTrace(Throwable ex)
    {
        StringBuilder strBuilder = new StringBuilder();
        StackTraceElement[] arrStackTrace = ex.getStackTrace();
        int maxLines = 20, maxLength = maxLines < arrStackTrace.length ? maxLines : arrStackTrace.length;
        for( int index = 0; index < maxLength; ++index )
        {
            StackTraceElement stackTraceElement = arrStackTrace[index];
            strBuilder.append(stackTraceElement.toString()).append("\n");
        }
        if( arrStackTrace.length > maxLines )
        {
            strBuilder.append("...").append(arrStackTrace.length - maxLines).append(" more");
        }
        return strBuilder.toString();
    }

    public void debug(String doGetAuthenticationInfo) {
//        oLogger.setLevel(getLogLevelFromDB());
        if(getLogLevelFromDB() <= Level.DEBUG_INT) oLogger.debug(doGetAuthenticationInfo);
        
    }

//    private Level getLogLevelFromDB()
    private int getLogLevelFromDB()
    {
        try {
            /* Standard levels in log4j, ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF.
            A log request of level p in a logger with level q, is enabled if p >= q. */
            
            String logLevel = null;
            
            Object obj = null;
//            obj = KwizolJDBCHelper.getLogLevelFromDB("SELECT propvalue FROM kwizolproperties WHERE propkey = '" + KwizolConfig.KEY_LOG_LEVEL + "'");
            
            if(obj == null || obj.toString().isEmpty()) 
//                return Level.ALL;
                return Level.ALL_INT;
            
            logLevel = obj.toString();
//            //System.out.println("log level : " + logLevel);
            
            if(logLevel == null || logLevel.isEmpty()) 
//                return Level.ALL;
                return Level.ALL_INT;
            
            if(Integer.parseInt(logLevel) <= WalletConstants.LOG_TYPE_DEBUG) 
//                return Level.DEBUG;
                return Level.DEBUG_INT;
            
            if(Integer.parseInt(logLevel) <= WalletConstants.LOG_TYPE_INFO) 
//                return Level.INFO;
                return Level.INFO_INT;
            
            if(Integer.parseInt(logLevel) <= WalletConstants.LOG_TYPE_ERROR) 
//                return Level.ERROR;
                return Level.ERROR_INT;
            
            if(Integer.parseInt(logLevel) <= WalletConstants.LOG_TYPE_FATAL) 
//                return Level.FATAL;
                return Level.FATAL_INT;
            
//            return Level.OFF;
            return Level.OFF_INT;
        } 
        catch (Exception ex) {
            java.util.logging.Logger.getLogger(WalletLogger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
//        return Level.ERROR;
        return Level.ERROR_INT;
        
    }
}