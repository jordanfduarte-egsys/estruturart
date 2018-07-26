package br.com.estruturart.model;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import br.com.estruturart.utility.ParamRequestManager;

public abstract class AbstractModel
{
    private ParamRequestManager validationMessages = new ParamRequestManager();
    private SimpleDateFormat simpleDateFormat;
    private NumberFormat numberFormat;

    public abstract boolean isValid() throws SQLException;

    public ParamRequestManager getValidation()
    {
        return this.validationMessages;
    }

    /**
     * @return the simpleDateFormat
     */
    public SimpleDateFormat getSimpleDateFormat(String pattern)
    {
        return simpleDateFormat = new SimpleDateFormat(pattern);
    }

    /**
     * @param simpleDateFormat the simpleDateFormat to set
     */
    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat)
    {
        this.simpleDateFormat = simpleDateFormat;
    }

    public String formatMoney(float money)
    {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        }

        return numberFormat.format(money).replace("R$", "").trim();
    }
}
