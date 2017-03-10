package com.caro.smartmodule.helpers;

public class AccountAuthHelper
{
    private AccountAuthHelper()
    {
    }
    
    public static final int TYPE_NONE = 0;
    public static final int TYPE_EMAIL = 1;
    public static final int TYPE_PHONE = 2;
    
    public static int getAccountType(String account)
    {

        
        if (isEmail(account))
        {
            return TYPE_EMAIL;
        }

        if (account.length() ==11){
            return TYPE_PHONE;
        }
        return TYPE_NONE;
    }
    
    public static boolean isEmail(String strEmail)
    {
        String strs[] = strEmail.split("@");
        if (strs.length != 2)
        {
            return false;
        }
        char firstChar = strEmail.charAt(0);
        if (firstChar == '@' || firstChar == '.' || firstChar == '+')
        {
            return false;
        }
        char lastChar = strEmail.charAt(strEmail.length() - 1);
        if (lastChar == '@' || lastChar == '.')
        {
            return false;
        }
        
        char atFrontChar = strs[0].charAt(strs[0].length() - 1);
        char atBehindChar = strs[1].charAt(0);
        if (atFrontChar == '.' || atFrontChar == '+')
        {
            return false;
        }
        if (atBehindChar == '.')
        {
            return false;
        }
        
        return true;
    }
}
