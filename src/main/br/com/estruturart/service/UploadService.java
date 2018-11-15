package br.com.estruturart.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import br.com.estruturart.utility.Exception1001;

public class UploadService
{
    private HttpServletRequest request;
    private String messageErro = "";
    private FileItem fileItem;
    private String[] extensoes;
    private String folder;
    public static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB

    public UploadService(HttpServletRequest request)
    {
        this.request = request;
    }

    public InputStream redimencionar(InputStream is, int width, int height) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(is);
        double thumbRatio = (double) width / (double) height;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            height = (int) (width / imageRatio);
        } else {
            width = (int) (height * imageRatio);
        }
        BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = thumb.createGraphics();
        graphic.drawImage(image, 0, 0, width, height, null);

        ImageIO.write(thumb, "PNG", baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    public boolean isUploadImagem() throws FileUploadException
    {
        return fileItem != null && !fileItem.isFormField() && !fileItem.getString().equals("");
    }

    public String process(String folder, int width, int height, String nomeAntigo, String folderCopy) throws IOException, NoSuchAlgorithmException
    {
        String fileName = "";
        if (ServletFileUpload.isMultipartContent(this.request)) {
            try {

                if (!isUploadImagem()) {
                    throw new Exception1001("Nenhum arquivo informado no upload. Selecione um arquivo novamente!");
                }

                /* Escreve a o arquivo na pasta img */
                FileItem item = fileItem;

                fileName = item.getName();

                InputStream fileContent = item.getInputStream();
                String contentType = item.getContentType();
                int index = fileName.lastIndexOf(".");
                String format = "";

                if (index > 0) {
                    format = fileName.substring(index + 1);
                    format = format.toLowerCase();
                }


                boolean isFormatValid = false;
                String extensoesAceitas = "";
                for (int i = 0; i < this.extensoes.length; i++) {
                    String str = (extensoes[i]);
                    if (str.trim().equals(format.toLowerCase())) {
                        isFormatValid = true;
                    }

                    if (i == 0) {
                        extensoesAceitas += str.trim();
                    } else {
                        extensoesAceitas += "," + str.trim();
                    }
                }


                if (!isFormatValid) {
                    throw new Exception1001("Formato do arquivo inválido, formatos aceitos " + extensoesAceitas + ".");
                }


                if (fileName.contains(System.getProperty("file.separator"))) {

                    fileName = fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator")));
                }

                if (fileName.equals("")) {
                    throw new Exception1001(
                        "Nome do arquivo informado inválido. Tente enviar um arquivo com nome correto!"
                    );
                }

                if (contentType.contains("image")) {
                    fileContent = redimencionar(fileContent, width, height);
                } else {
                    throw new Exception1001(
                        "Nome do arquivo informado inválido. Tente enviar um arquivo com nome correto!"
                    );
                }
                fileName = savePosRedimencionamento(fileName, format, folder, nomeAntigo, fileContent, folderCopy);
                // MessageDigest algorithm = MessageDigest.getInstance("MD5");
                // String messageDigest = convertStringToMd5(fileName + new Date().getTime());
                // fileName = messageDigest + "." + format;
                //

                // String folderTo = folder +  getFolder();
                // File uploadDir = new File(folderTo);
                // if (!uploadDir.exists() || !uploadDir.isDirectory()) {
                //     uploadDir.mkdirs();
                //
                // }
                //
                //
                // FileOutputStream fout = new FileOutputStream(folderTo + fileName);
                //
                // byte[] buffer = new byte[fileContent.available()];
                // fileContent.read(buffer);

                // fout.write(buffer);
                // fout.flush();
                // fout.close();

                // if (!uploadDir.exists()) {
                //     fileName = "";
                //     throw new Exception1001("Ocorre um erro ao realizar o upload");
                // }

                // if (nomeAntigo != null) {
                //     String folderToDelete = folder + getFolder() + nomeAntigo;
                //     File uploadDirDelete = new File(folderToDelete);
                //     if (uploadDirDelete.exists()) {
                //         uploadDirDelete.delete();
                //     }
                // }
            } catch (FileUploadException e) {
                fileName = "";

                this.setMessageErro("Ocorreu um erro ao salvar o arquivo no diretório. Informe a equipe de suporte!");
            } catch (Exception1001 e) {
                fileName = "";
                e.printStackTrace();

                this.setMessageErro(e.getMessage());
            } catch (Exception e) {
                fileName = "";
                e.printStackTrace();

                this.setMessageErro("Ocorreu um erro ao realizar o upload. Tente novamente!");
            }
        }

        return fileName;
    }

    private String convertStringToMd5(String valor)
    {
       MessageDigest mDigest;
       try {
          //Instanciamos o nosso HASH MD5, poderamos usar outro como
          //SHA, por exemplo, mas optamos por MD5.
         mDigest = MessageDigest.getInstance("MD5");

         //Convert a String valor para um array de bytes em MD5
         byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));

         //Convertemos os bytes para hexadecimal, assim podemos salvar
         //no banco para posterior comparo se senhas
         StringBuffer sb = new StringBuffer();
         for (byte b : valorMD5) {
                sb.append(Integer.toHexString((b & 0xFF) |
                0x100).substring(1,3));
         }

         return sb.toString();
       } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
       } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
       }

       return null;
    }

    /**
     * @return the messageErro
     */
    public String getMessageErro()
    {
        return messageErro;
    }

    /**
     * @param messageErro
     *            the messageErro to set
     */
    public void setMessageErro(String messageErro)
    {
        this.messageErro = messageErro;
    }

    /**
     * @return the extensoes
     */
    public String[] getExtensoes()
    {
        return extensoes;
    }

    /**
     * @param extensoes
     *            the extensoes to set
     */
    public void setExtensoes(String[] extensoes)
    {
        this.extensoes = extensoes;
    }

    /**
     * @return the fileItem
     */
    public FileItem getFileItem()
    {
        return fileItem;
    }

    /**
     * @param fileItem
     *            the fileItem to set
     */
    public void setFileItem(FileItem fileItem)
    {
        this.fileItem = fileItem;
    }

    public String getFolder()
    {
        return this.folder;
    }

    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public String savePosRedimencionamento(String fileName, String format, String folder, String nomeAntigo, InputStream fileContent, String folderCopy) throws IOException, java.security.NoSuchAlgorithmException, Exception1001
    {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        String messageDigest = convertStringToMd5(fileName + new Date().getTime());
        fileName = messageDigest + "." + format;


        String folderTo = folder +  getFolder();
        String folderToCopy = folderCopy +  getFolder();
        File uploadDir = new File(folderTo);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            uploadDir.mkdirs();

        }

        File uploadDir2 = new File(folderToCopy);
        if (!uploadDir2.exists() || !uploadDir2.isDirectory()) {
            uploadDir2.mkdirs();
        }

        FileOutputStream fout = new FileOutputStream(folderTo + fileName);
        byte[] buffer = new byte[fileContent.available()];
        fileContent.read(buffer);

        fout.write(buffer);
        fout.flush();
        fout.close();
        fileContent.close();

        //copiando para a pasta do projeto
        File file2 = new File(folderTo + fileName);
        file2.setReadable(true, false);
        file2.setExecutable(true, false);
        file2.setWritable(true, false);
        FileInputStream fis2 = new FileInputStream(file2);
        BufferedInputStream bis2 = new BufferedInputStream(fis2);

        File file3 = new File(folderToCopy + fileName);
        file3.setReadable(true, false);
        file3.setExecutable(true, false);
        file3.setWritable(true, false);
        FileOutputStream fis3 = new FileOutputStream(file3);
        BufferedOutputStream bis3 = new BufferedOutputStream(fis3);

        int count = 0;

        byte[] bytes = new byte[1024];

        while((count = bis2.read(bytes)) > 0) {
            bis3.write(bytes, 0, count);
        }

        bis2.close();
        bis3.close();
        //FIM copiando para a pasta do projeto

        if (!uploadDir.exists()) {
            fileName = "";
            throw new Exception1001("Ocorre um erro ao realizar o upload");
        }

        if (nomeAntigo != null) {
            String folderToDelete = folder + getFolder() + nomeAntigo;
            File uploadDirDelete = new File(folderToDelete);

            if (uploadDirDelete.exists()) {
                uploadDirDelete.delete();
            }
        }

        return fileName;
    }
}
