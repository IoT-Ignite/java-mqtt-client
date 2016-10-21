package com.ardic.mqtt.client.wsclient;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ardic.mqtt.client.model.Dms;
import com.ardic.mqtt.client.util.Constants;

public class DmsInformationServiceClient {

	Logger logger = LoggerFactory.getLogger(DmsInformationServiceClient.class);

	public Dms getAvailableDms(String deviceId) {
		String domain = null;
		int port = 0;
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// Send SOAP Message to SOAP Server
			String url = Constants.DMS_INFORMATION_SERVICE;
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(deviceId), url);

			ResponseParser parser = new ResponseParser();
			parser.parse(printSOAPResponse(soapResponse));

			domain = parser.getServiceAddress();
			port = parser.getServicePort();

			logger.debug("Domain: " + domain + " Port: " + port);
			soapConnection.close();
		} catch (SOAPException e2) {
			logger.error("SOAPException", e2);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return new Dms(domain, port);
	}

	private static SOAPMessage createSOAPRequest(String deviceId) throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String serverURI = "http://dms.arcsp.ardic.com";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("dms", serverURI);

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapGetAvailableDms = soapBody.addChildElement("getAvailableDms", "dms");
		SOAPElement soapDeviceId = soapGetAvailableDms.addChildElement("deviceId", "dms");
		soapDeviceId.addTextNode(deviceId);
		SOAPElement soapConnectionType = soapGetAvailableDms.addChildElement("connectionType", "dms");
		soapConnectionType.addTextNode("mqtt");

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + "getAvailableDms");

		soapMessage.saveChanges();

		return soapMessage;
	}

	private static String printSOAPResponse(SOAPMessage soapResponse) throws SOAPException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		StringWriter writer = new StringWriter();
		transformer.transform(sourceContent, new StreamResult(writer));
		return writer.getBuffer().toString();
	}

	class ResponseParser {

		private String serviceAddress;
		private int servicePort;

		private void parse(String response) {
			reset();
			XmlPullParserFactory factory;
			try {
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(new StringReader(response));
				int eventType = xpp.getEventType();

				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {

						if (xpp.getName().equalsIgnoreCase("port")) {
							String str = xpp.nextText();
							if (str != null && !str.equals("")) {
								servicePort = Integer.valueOf(str);
							}

						} else if (xpp.getName().equalsIgnoreCase("domain")) {
							String str = xpp.nextText();
							if (str != null && !str.equals("")) {
								serviceAddress = str;
							}

						}
					}
					eventType = xpp.next();
				}
			} catch (XmlPullParserException e) {
				logger.error("SOAP Response XML Parse error", e);
			} catch (IOException e) {
				logger.error("SOAP Response XML IOexception", e);
			}
		}

		private void reset() {
			serviceAddress = null;
			servicePort = 0;
		}

		public boolean isSuccessful() {
			return getServiceAddress() != null && getServicePort() != 0;
		}

		public String getServiceAddress() {
			return serviceAddress;
		}

		public int getServicePort() {
			return servicePort;
		}
	}

}
