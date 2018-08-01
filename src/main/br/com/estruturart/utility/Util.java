package br.com.estruturart.utility;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.text.SimpleDateFormat;
import br.com.estruturart.utility.StringUtilsPad;

public class Util
{
    public static boolean isDateValid(String strDate)
    {
        String dateFormat = "dd/MM/uuuu";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern(dateFormat)
            .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate date = LocalDate.parse(strDate, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
           return false;
        }
    }

    public static Date toDate(String dataStr) throws java.text.ParseException
    {
        Date data = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(dataStr);
    }

    public static boolean isCPFValid(String CPF)
    {
        CPF = CPF.replace(".", "").replace("-", "");
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
           (CPF.length() != 11))
           return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;

            for (i=0; i<9; i++) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static boolean isCNPJValid(String CNPJ)
    {
        CNPJ = CNPJ.replace(".", "").replace("-", "").replaceAll("/", "");
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
            CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
            CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
            CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
            CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
           (CNPJ.length() != 14))
           return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                // converte o i-simo caractere do CNPJ em um nmero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posio de '0' na tabela ASCII)
                num = CNPJ.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                   peso = 2;
                  }

                  r = sm % 11;
                  if ((r == 0) || (r == 1))
                      dig13 = '0';
                  else dig13 = (char)((11-r) + 48);

                  // Calculo do 2o. Digito Verificador
                  sm = 0;
                  peso = 2;
                  for (i=12; i>=0; i--) {
                      num = CNPJ.charAt(i)- 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                   peso = 2;
              }

              r = sm % 11;
              if ((r == 0) || (r == 1))
                 dig14 = '0';
              else dig14 = (char)((11-r) + 48);

              // Verifica se os dgitos calculados conferem com os dgitos informados.
              if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                 return(true);
              else return(false);
            } catch (InputMismatchException erro) {
                return(false);
        }
    }

    public static boolean isNomeCompletoValid(String nome)
    {
        String[] list;

        list = nome.split(" ");

        if (list.length <= 1) {
            return false;
        }

        if (list[1].length() <= 1) {
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email)
    {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }

        return isEmailIdValid;
    }

    public static boolean isPasswordValid(String senha)
    {
        if (senha.length() < 6) {
            return false;
        }

        String especiais[] = {"@", "-", ".", "_", "&", "%", "$", "#", "!", "?"};
        int countEspecial = 0;
        for (int i = 0 ; i < senha.length(); i++) {
            for (int y = 0 ; y < especiais.length; y++) {
                if (Character.toString(senha.charAt(i)).equals(especiais[y])) {
                    countEspecial++;
                }
            }
        }

        if (countEspecial == 0 || countEspecial > 3) {
            return false;
        }

        return true;
    }

    public static String dataBrToEn(String dataBr)
    {
        String[] aDataBr = dataBr.split("/");

        if (aDataBr.length == 3) {
            return String.format(
                "%s-%s-%s", aDataBr[2], StringUtilsPad.padLeft(aDataBr[1], 2, "0"), aDataBr[0]
            );
        }

        return "";
    }
}
