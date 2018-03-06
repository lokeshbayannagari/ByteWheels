package com.digiwallet.exception;

import java.text.MessageFormat;
import javax.ejb.ApplicationException;
import net.sf.json.JSONObject;

@ApplicationException(rollback=true)
public class WalletException extends Exception
{
    private static final long serialVersionUID = -928753111079583588L;
    
    public static enum Codes
    {
        CODE_CONFIG_PROPERTIES_FILE(901, "Properties file path not set"),
        CODE_CONFIG_PROPERTIES_NOT_SET(902, "Project property not set"),
        
        CODE_NULL_ENTITY(500, "Entity not present"),
        CODE_INTERNAL_SYSTEM_ERROR(0, "Internal System Error, Check Log for Details."),
        
        CODE_VERSION_MISMATCH(1, "Version mismatch while saving, please refresh data before saving."),
        CODE_NO_PERMISSION(4, "You do not have enough permissions to perform this operation."),
        CODE_ERROR_SECURITY(5, "Security error"),
        CODE_ENTITY_NOT_DELETABLE(6, "{0} cannot be deleted as it is used in other places"),
        CODE_INVALID_OPERATION(7, "Error! Invalid operation performed."),
        CODE_INVALID_ARGUMENTS(8, "Error! Invalid arguments received."),
//        CODE_RESULTS_NOT_READY(9, "The paper is not yet evaluated. For results, please contact the assessor."),
        CODE_RESULTS_NOT_READY(9, ""),
        
        /* DB related */
        CODE_DB_CONNECTION_OPEN(1001, "Unable to open connection to the database"),
        CODE_DB_CONNECTION_CLOSE(1002, "Unable to close the connection of the database"),
        CODE_DB_QUERY_EXECUTION(1003, "Unable to execute the given query"),
        CODE_DB_NO_RESULTS(1004, "No records found!"),
        //CODE_DB_DATA_CHANGED(1005, "Data has changed since it was last read. Please reload the data and try again."),
        CODE_DB_DATA_CHANGED(1005, " "),
        
        /* JSON */
        CODE_BAD_JSON_STRING(1100, "JSON string is malformed. Cannot be parsed"),
        
    
        /*** Users******/
        CODE_USERS_DUPLICATE_EMAILID(10012, "Email ID already exists in the system"),
        CODE_USERS_DUPLICATE_EMAILID_IN_CURRENT_ORGANIZATION(10013, "Email ID already exists in the selected organization."),
        CODE_USERS_PASSWORD_CANNOT_BE_SET_DUPLICATE_EMAILID(10014, "User already exists in other organization(s). Password cannot be set."),
        CODE_USERS_FAILED_TO_SEND_EMAIL(10015, "Failed to send mail."),
        CODE_USERS_DUPLICATE_UNIQUEID(10016, "Unique ID already exists. Please choose a different one."),
      
        
        
        /* Date */
        CODE_UNPARSEABLE_DATE(11000, "Invalid date"),
        
        /**** Captcha ****/
        CODE_INVALID_CAPTCHA(12000, "Invalid captcha characters entered. Please try again"),
        
        /**** Registration not supported ****/
        CODE_REGISTRATION_NOT_SUPPORTED(13000, "Registration is not supported yet"),
        CODE_SUBSCRIPTION_NOT_SUPPORTED(14000, "Subscription is not supported yet"),
        
        /*** ***/
        CODE_ENTITY_DUPLICATE_NAME(15000, "Name already exists! Duplicates are not allowed"),
        
        
        /*** Mail ***/
        CODE_FAILED_TO_SEND_MAIL(18000, "Unable to send mail due to some error."),
        
        /*** oAuth authentication ***/
        CODE_OAUTH_AUTHENTICATION_FAILED(19000, "Authentication with oAuth server failed.");
        
        private final int code;
        private final String internalMessage;
        private final String userMessage;
        Codes(int code, String internalMessage)
        {
            this.code = code;
            this.internalMessage = internalMessage;
            this.userMessage = internalMessage;
        }
        Codes(int code, String internalMessage, String userMessage)
        {
            this.code = code;
            this.internalMessage = internalMessage;
            this.userMessage = userMessage;
        }
    }
    
    /**
     * The error code for the exception.  Used for custom string lookups.
     */
    private Codes code;
    public Codes getCode()              {return code;}
    public void setCode(Codes code)     {this.code = code;}
    
    private String internalMessage;
    public String getInternalMessage()                      {return internalMessage;}
    public void setInternalMessage(String internalMessage)  {this.internalMessage = internalMessage;}
    
    private String userMessage;
    public String getUserMessage()                      {return userMessage;}
    public void setUserMessage(String userMessage)      {this.userMessage = userMessage;}

    @Override
    public String getMessage()      {return this.internalMessage;}

    /** 
     * Constructor
     * Creates a new exception with error code = code passed in
     * @param code integer representation of the error code
     */
    public WalletException(Codes code)
    {
        //super(getUserExceptionString(code, new ArrayList()));
        this.code = null != code ? code : Codes.CODE_INTERNAL_SYSTEM_ERROR;
        this.internalMessage = getInternalMessageString(this.code.code, this.code.internalMessage, null);
        this.userMessage = getUserMessageString(this.code.code, this.code.userMessage);
    }
    
    /** 
     * Constructor
     * Creates a new exception with error code = code passed in
     * @param exception the nested exception
     */
    public WalletException(Throwable exception)
    {
        this.code = Codes.CODE_INTERNAL_SYSTEM_ERROR;
        this.internalMessage = getInternalMessageString(this.code.code, this.code.userMessage + " : " + exception.getMessage(), exception);
        this.userMessage = getUserMessageString(this.code.code, this.code.userMessage);
    }

    /** 
     * Constructor
     * Creates a new exception with error code = code passed in
     * @param code integer representation of the error code
     * @param exception the nested exception
     */
    public WalletException(Codes code, Throwable exception)
    {
        this.code = null != code ? code : Codes.CODE_INTERNAL_SYSTEM_ERROR;
        this.internalMessage = getInternalMessageString(this.code.code, this.code.internalMessage, exception );
        this.userMessage = getUserMessageString(this.code.code, this.code.userMessage);
    }
    
    /** 
     * Constructor
     * Creates a new exception with error code = code passed in
     * @param code integer representation of the error code
     * @param exception the nested exception
     * @param arguments the parameters for the message
     */
    public WalletException(Codes code, Throwable exception, Object... arguments)
    {
        this.code = null != code ? code : Codes.CODE_INTERNAL_SYSTEM_ERROR;
        this.internalMessage = getInternalMessageString(this.code.code, this.code.internalMessage, exception, arguments);
        this.userMessage = getUserMessageString(this.code.code, this.code.userMessage, arguments);
    }
    
    /** 
     * Constructor
     * Creates a new exception with error code = code passed in
     * @param code integer representation of the error code
     * @param arguments the parameters for the message
     */
    public WalletException(Codes code, Object... arguments)
    {
        this.code = null != code ? code : Codes.CODE_INTERNAL_SYSTEM_ERROR;
        this.internalMessage = getInternalMessageString(this.code.code, this.code.internalMessage, null, arguments);
        this.userMessage = getUserMessageString(this.code.code, this.code.userMessage, arguments);
    }
    
    public static String getUserMessage(Codes code)
    {
        return new JSONObject().element("err", true).element("code", code.code).element("message", code.userMessage).toString();
    }
    
    public static String getUserMessage(Codes code, Object... params)
    {
        return new JSONObject().element("err", true).element("code", code.code).element("message", MessageFormat.format( code.userMessage, params )).toString();
    }
    
    public static String getInternalMessageString(int code, String strMessage, Throwable ex, Object... params)
    {
        return "ERROR CODE : " + code + " : " + MessageFormat.format( strMessage, params ) + (null != ex ? " Details : " + ex.getMessage() : "");
    }
    
    public static String getUserMessageString(int code, String strMessage, Object... params)
    {
        return new JSONObject().element("err", true).element("code", code).element("message", MessageFormat.format( strMessage, params )).toString();
    }
}