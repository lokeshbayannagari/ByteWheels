package com.bytewheels.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class InvoiceTemplateGenerator {

	public static String generateHtmlTemplate(Map<String, String> bookingDetails) {
		String htmlMinified = "<html><body style=\"\"> <div style=\"border:1px solid #007dc3;width:650px;height:350px;\"> <div style=\"height:60px;background-color:#007dc3\"><div style=\"width:100%;height:60%;color:white;font-size:30px;padding-top:15px;padding-bottom:15px;text-align:center\">Invoice</div></div><div style=\"padding:10px\"> <div >Dear  {0},</div><div> <div style=\"margin-top:15px\"> <span style=\"display:inline-block; width: 40px;\"></span>Your booking has been confirmed!! </div><div style=\"margin-top:15px\" > Here is the details for your booking id{1}. We hope you are satisfied with our service.<br/> If you have any queries, you can contact us at support@bytewheels.com. </div></div><table style=\"margin-top:20px ; border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\"> <tr style=\"vertical-align: middle;\"> <td style=\" border: 1px solid #007dc3; border-collapse: collapse; text-align: center; background-color: #007dc3; color: white; border: 1px solid black;padding:10px\">Vehicle Model</td><td style=\" border: 1px solid #007dc3; border-collapse: collapse; text-align: center;background-color: #007dc3; color: white; border: 1px solid black;padding:10px\">Vehicle Number</td><td style=\" border: 1px solid #007dc3; border-collapse: collapse; text-align: center;background-color: #007dc3; color: white; border: 1px solid black;padding:10px\">From Date</td><td style=\" border: 1px solid #007dc3; border-collapse: collapse; text-align: center;background-color: #007dc3; color: white; border: 1px solid black;padding:10px\">To Date</td><td style=\" border: 1px solid #007dc3; border-collapse: collapse; text-align: center;background-color: #007dc3; color: white; border: 1px solid black;padding:10px\">Total Cost</td></tr><tr style=\"vertical-align: middle;\"> <td style=\"border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\">{2}</td><td style=\"border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\">{3}</td><td style=\"border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\">{4}</td><td style=\"border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\">{5}</td><td style=\"border: 1px solid #007dc3; border-collapse: collapse; text-align: center;\"><b>${6}</b></td></tr></table> <div style=\"margin-top:35px\">Thank you for using ByteWheels</div></div></div></body></html>";
		return MessageFormat.format(htmlMinified, bookingDetails.get("name"), bookingDetails.get("bookingID"),
				bookingDetails.get("vehicleModel"), bookingDetails.get("vehicleNo"), bookingDetails.get("fromDate"),
				bookingDetails.get("toDate"), bookingDetails.get("totalCost"));
	}

}
