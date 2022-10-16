/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.jost_net.JVerein.rmi.MailAnhang;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Level;
import de.willuhn.logging.Logger;

public class MailSender
{
  private static final String UTF_8 = "UTF-8";

  public static class IMAPCopyData
  {
    private final boolean copy_to_imap_folder;

    private final String imap_auth_user;

    private final String imap_auth_pwd;

    private final String imap_host;

    private final String imap_port;

    private final boolean imap_ssl;

    private final boolean imap_starttls;

    private final String imap_sent_folder;

    public IMAPCopyData(boolean copy_to_imap_folder, String imap_auth_user,
        String imap_auth_pwd, String imap_host, String imap_port,
        boolean imap_ssl, boolean imap_starttls, String imap_sent_folder)
    {
      this.copy_to_imap_folder = copy_to_imap_folder;
      this.imap_auth_user = imap_auth_user;
      this.imap_auth_pwd = imap_auth_pwd;
      this.imap_host = imap_host;
      this.imap_port = imap_port;
      this.imap_ssl = imap_ssl;
      this.imap_starttls = imap_starttls;
      this.imap_sent_folder = imap_sent_folder;
    }

    public boolean isCopy_to_imap_folder()
    {
      return copy_to_imap_folder;
    }

    public String getImap_auth_user()
    {
      return imap_auth_user;
    }

    public String getImap_auth_pwd()
    {
      return imap_auth_pwd;
    }

    public String getImap_host()
    {
      return imap_host;
    }

    public String getImap_port()
    {
      return imap_port;
    }

    public boolean isImap_ssl()
    {
      return imap_ssl;
    }

    public boolean isImap_starttls()
    {
      return imap_starttls;
    }

    public String getImap_sent_folder()
    {
      return imap_sent_folder;
    }
  }

  private final String smtp_auth_user;

  private final String smtp_auth_pwd;

  private final String smtp_from_address;

  private final String smtp_from_anzeigename;

  /** If set, all mails will be sent to this BCC, too */
  private final String bcc_address;

  /** If set, all mails will be sent to this BCC, too */
  private final String cc_address;

  private final int verzoegerung;

  private final IMAPCopyData imapCopyData;

  private final Session session;

  public MailSender(String smtp_host_name, String smtp_port,
      String smtp_auth_user, String smtp_auth_pwd, String smtp_from_address,
      String smtp_from_anzeigename, String bcc_address, String cc_address,
      boolean smtp_ssl, boolean smtp_starttls, int verzoegerung,
      IMAPCopyData imapCopyData)
  {
    this.smtp_from_address = smtp_from_address;
    this.smtp_from_anzeigename = smtp_from_anzeigename;
    this.bcc_address = bcc_address;
    this.cc_address = cc_address;
    this.verzoegerung = verzoegerung;

    Properties props = new Properties();
    if (smtp_auth_user != null && smtp_auth_user.length() != 0
        && smtp_auth_pwd != null && smtp_auth_pwd.length() != 0)
    {
      props.put("mail.smtp.auth", "true");
      this.smtp_auth_user = smtp_auth_user;
      this.smtp_auth_pwd = smtp_auth_pwd;
    }
    else
    {
      props.put("mail.smtp.auth", "false");
      this.smtp_auth_user = null;
      this.smtp_auth_pwd = null;
    }
    props.put("mail.smtp.host", smtp_host_name);
    props.put("mail.smtp.localhost", "localhost");
    if (Application.getConfig().getLogLevel().equals(Level.DEBUG.getName()))
    {
      props.put("mail.debug", "true");
    }
    if (smtp_port != null)
    {
      props.put("mail.smtp.port", smtp_port);
    }
    props.put("mail.mime.charset", UTF_8);
    System.setProperty("mail.mime.charset", UTF_8);
    if (smtp_ssl)
    {
      props.put("mail.smtp.ssl.enable", "true");
    }
    if (smtp_starttls)
    {
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.tls", "true");
    }
    if (imapCopyData != null && imapCopyData.isCopy_to_imap_folder())
    {
      String protocol = "imap";
      if (imapCopyData.isImap_ssl())
      {
        protocol = "imaps";
      }
      else if (imapCopyData.isImap_starttls())
      {
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.imap.tls", "true");
      }
      props.put("mail.store.protocol", protocol);
      props.put("mail."+protocol+".host", imapCopyData.getImap_host());
      if (imapCopyData.getImap_port() != null)
      {
        props.put("mail."+protocol+".port", imapCopyData.getImap_port());
      }
      this.imapCopyData = imapCopyData;
    }
    else
    {
      this.imapCopyData = null;
    }

    this.session = Session.getInstance(props);
  }

  // Send to a single recipient
  public void sendMail(String email, String subject, String text,
      TreeSet<MailAnhang> anhang) throws Exception
  {
    String[] emailList = new String[1];
    emailList[0] = email;
    sendMail(emailList, subject, text, anhang);
  }

  // //Send to multiple recipients

  public void sendMail(String[] emailadresses, String subject, String text,
      TreeSet<MailAnhang> anhang) throws Exception
  {
    Message msg = new MimeMessage(session);
    /*
     * msg.addHeader("Disposition-Notification-To", smtp_from_address);
     * msg.addHeader("Return-Receipt-To", smtp_from_address);
     */
    InternetAddress addressFrom = new InternetAddress(smtp_from_address,
        smtp_from_anzeigename);
    msg.setFrom(addressFrom);

    InternetAddress[] addressTo = new InternetAddress[emailadresses.length];

    for (int i = 0; i < emailadresses.length; i++)
    {
      addressTo[i] = new InternetAddress(emailadresses[i]);
    }

    msg.setRecipients(Message.RecipientType.TO, addressTo);
    if (bcc_address != null && !bcc_address.trim().isEmpty())
    {
      msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(
          bcc_address.trim()));
    }
    if (cc_address != null && !cc_address.trim().isEmpty())
    {
      msg.setRecipient(Message.RecipientType.CC,
          new InternetAddress(cc_address.trim()));
    }
    msg.setSubject(subject);

    // Man koennte dies auch per Checkbox im Formular abfragen
    boolean html = text.toLowerCase().contains("<html");

    MimeBodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.addHeader("Content-Encoding", UTF_8);
    Multipart multipart = new MimeMultipart("mixed");

    // Fill the message
    if (html)
    {
      messageBodyPart.setText(text, UTF_8, "html");

      Multipart alternativeMessagesMultipart = new MimeMultipart("alternative");

      MimeBodyPart altMessageBodyPart = new MimeBodyPart();
      altMessageBodyPart.addHeader("Content-Encoding", UTF_8);
      altMessageBodyPart
          .setText(
              "Um diese Email richtig darstellen zu können, erlauben Sie bitte in Ihrem Emailprogramm die Darstellung von HTML-Emails.\n"
                  + text
                      .replaceAll(
                          "(?s)<\\s*?(script|Script|SCRIPT).*?>.*?</\\s*?(script|Script|SCRIPT)\\s*?>",
                          "")
                      .replaceAll(
                          "(?s)<\\s*?(style|Style|STYLE).*?>.*?</\\s*?(style|Style|STYLE)\\s*?>",
                          "").replaceAll("<.*?>", "")
                      .replaceAll("\\n{4,}", "\n\n\n").replaceAll("\\t", ""),
                      UTF_8);

      alternativeMessagesMultipart.addBodyPart(altMessageBodyPart);
      alternativeMessagesMultipart.addBodyPart(messageBodyPart);

      MimeBodyPart alternativeMessagesBodyPart = new MimeBodyPart();
      alternativeMessagesBodyPart.setContent(alternativeMessagesMultipart);
      multipart.addBodyPart(alternativeMessagesBodyPart);
    }
    else
    {
      messageBodyPart.setText(text, UTF_8);
      multipart.addBodyPart(messageBodyPart);
    }

    for (MailAnhang ma : anhang)
    {
      messageBodyPart = new MimeBodyPart();

      messageBodyPart.setDataHandler(new DataHandler(
          new ByteArrayDataSource(ma)));
      messageBodyPart.setFileName(ma.getDateiname());
      multipart.addBodyPart(messageBodyPart);
    }
    // Put parts in message
    msg.setContent(multipart);

    // need to set "sent" date explicitly
    msg.setSentDate(new Date());

    Transport.send(msg, smtp_auth_user, smtp_auth_pwd);

    // Copy to IMAP sent folder
    if (imapCopyData != null)
    {
      Store store = session.getStore();
      store.connect(imapCopyData.getImap_auth_user(), imapCopyData.getImap_auth_pwd());
      Folder folder = store.getFolder(imapCopyData.getImap_sent_folder());
      folder.appendMessages(new Message[] { msg });
      store.close();
    }
    Thread.sleep(verzoegerung);
  }

  private static class ByteArrayDataSource implements DataSource
  {

    private final MailAnhang ma;

    public ByteArrayDataSource(MailAnhang ma)
    {
      this.ma = ma;
    }

    @Override
    public String getContentType()
    {
      try
      {
        return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(
            ma.getDateiname());
      }
      catch (RemoteException e)
      {
        Logger.error("", e);
      }
      return null;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
      return new ByteArrayInputStream(ma.getAnhang());
    }

    @Override
    public String getName()
    {
      String name = null;
      try
      {
        name = ma.getDateiname();
      }
      catch (RemoteException e)
      {
        Logger.error("", e);
      }
      return name;
    }

    @Override
    public OutputStream getOutputStream()
    {
      return new ByteArrayOutputStream();
    }

  }
}