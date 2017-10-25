/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package utils;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

/**
 * Class for email handling
 *
 */
public class Email {
	
	/**
	 * Checks if confirmation email for new contract has been sent
	 * 
	 * @return boolean    true if email found, false if not
	 */
	public static boolean checkContractMail() {
		boolean ret = false;
		try{
			Properties props = new Properties();
			props.setProperty("mail.imaps.host", "imap.gmail.com");
			props.setProperty("mail.imaps.port", "993");
			props.setProperty("mail.imaps.connectiontimeout", "5000");
			props.setProperty("mail.imaps.timeout", "5000");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", "testautomation@meetingpackage.com", "meetingpackage123");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			SearchTerm term = new SearchTerm() {
			    public boolean match(Message message) {
			        try {
			        	String subj = message.getSubject();
			            if (subj.contains("Contract for supplier") && subj.contains("has been accepted")) {
			                return true;
			            }
			        } catch (MessagingException ex) {
			            ex.printStackTrace();
			        }
			        return false;
			    }
			};
			Message[] messages = inbox.search(term);
			if(messages.length==1)
				ret = true;
			else if(messages.length>1)
				System.out.println("More than one email met the criteria, cannot assert");
//			Message[] messages = inbox.getMessages();
			for (Message message : messages) {
				System.out.println("---------------------------------");
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
			}
			inbox.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Method to delete all mesages from inbox
	 */
	public static void deleteMessages() {
		try{
			Properties props = new Properties();
			props.setProperty("mail.imaps.host", "imap.gmail.com");
			props.setProperty("mail.imaps.port", "993");
			props.setProperty("mail.imaps.connectiontimeout", "5000");
			props.setProperty("mail.imaps.timeout", "5000");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", "testautomation@meetingpackage.com", "meetingpackage123");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			
			Message[] messages = inbox.getMessages();
//			inbox.copyMessages(msgs, store.getFolder("BIN"));
			Flags deletedFlag = new Flags(Flag.DELETED);
			inbox.setFlags(messages, deletedFlag, true);
			inbox.close(true);
			store.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
////			checkContractMail();
////	    	deleteMessages();
//	   }
}
