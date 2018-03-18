package com.bytewheels.daoimpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bytewheels.dao.VehicleDaoActions;
import com.bytewheels.entity.Booking;
import com.bytewheels.entity.Vehicle;
import com.bytewheels.entity.VehicleCategory;
import com.bytewheels.entity.VehicleModel;
import com.bytewheels.exception.ByteWheelsException;
import com.bytewheels.utils.ByteWheelsLogger;
import com.bytewheels.utils.InvoiceTemplateGenerator;
import com.bytewheels.utils.MailUtility;

@Stateless
public class VehicleDaoImpl implements VehicleDaoActions {
	ByteWheelsLogger logger = new ByteWheelsLogger(VehicleDaoImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String getCarDetails(Long fromDate, Long toDate) throws ByteWheelsException {
		final String METHOD_NAME = "getCarDetails";
		logger.logEntering(METHOD_NAME);

		JSONObject responseObject = new JSONObject();
		try {

			if (null == fromDate || null == toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}

			if (fromDate > toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			long todayInMilliSec = today.getTimeInMillis();
			if (todayInMilliSec > fromDate || todayInMilliSec > toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}

			StringBuilder strCarsQuery = new StringBuilder();
			SimpleDateFormat dtFrmt = new SimpleDateFormat("yyyy-MM-dd");
			String strStartDate = dtFrmt.format(fromDate);
			System.out.println(strStartDate);
			strCarsQuery.append("SELECT vc.vehiclecategoryid, vc.categoryname, ");
			strCarsQuery.append("vc.categorycost, vm.vehiclemodelid, vm.vehiclemodelname, ");
			strCarsQuery.append("vm.vehiclemodelcount - COUNT(b.vehicleid) AS count FROM vehicles v ");
			strCarsQuery.append("INNER JOIN vehiclecategories vc ON v.vehiclecategoryid = vc.vehiclecategoryid ");
			strCarsQuery.append("INNER JOIN vehiclemodels vm ON v.vehiclemodelid = vm.vehiclemodelid ");
			strCarsQuery.append("LEFT OUTER JOIN bookings b ON v.vehicleid = b.vehicleid ");
			strCarsQuery.append("WHERE (b.enddate IS NULL OR b.enddate >= '").append(strStartDate).append("') ");
			strCarsQuery.append("GROUP BY vc.categoryname,vm.vehiclemodelname; ");
			System.out.println(strCarsQuery.toString());

			Query query = this.entityManager.createNativeQuery(strCarsQuery.toString());

			List<Object[]> results = query.getResultList();
			System.out.println(results);
			System.out.println(results.size());
			if (results.isEmpty()) {
				responseObject.put("err", true);
				responseObject.put("message", "No vehicles found.");
			} else {
				JSONArray carDetailsArray = new JSONArray();
				JSONObject categoryJSON = null;
				String strPrevCategory = null;
				JSONArray modelArray = null;
				for (Object[] result : results) {
					if (null == strPrevCategory || !strPrevCategory.equalsIgnoreCase((String) result[1])) {
						if (null != modelArray && null != categoryJSON) {
							categoryJSON.put("models", modelArray);
						}
						modelArray = new JSONArray();
						categoryJSON = new JSONObject();
						categoryJSON.put("categoryid", result[0]);
						categoryJSON.put("categoryname", result[1]);
						categoryJSON.put("categorycost", result[2]);
						carDetailsArray.put(categoryJSON);
					}

					JSONObject modelJSON = new JSONObject();
					modelJSON.put("modelid", result[3]);
					modelJSON.put("modelname", result[4]);
					modelJSON.put("modelcount", result[5]);
					modelArray.put(modelJSON);

					strPrevCategory = (String) result[1];
				}

				categoryJSON.put("models", modelArray);

				responseObject.put("err", false);
				responseObject.put("data", carDetailsArray.toString());
			}

		} catch (JSONException e) {
			logger.logError(null, e);
			throw new ByteWheelsException(ByteWheelsException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			throw new ByteWheelsException(ByteWheelsException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}
		return responseObject.toString();
	}

	@Override
	@Transactional
	public String bookVehicle(Integer categoryId, Integer modelId, Long fromDate, Long toDate, String emailId)
			throws ByteWheelsException {
		// TODO Auto-generated method stub
		final String METHOD_NAME = "bookVehicle";
		logger.logEntering(METHOD_NAME);

		JSONObject responseObject = new JSONObject();
		try {
			if (null == fromDate || null == toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}

			if (null == modelId || null == categoryId || null == emailId) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_INVALID_ARGUMENTS);
			}

			if (fromDate > toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			long todayInMilliSec = today.getTimeInMillis();
			if (todayInMilliSec > fromDate || todayInMilliSec > toDate) {
				throw new ByteWheelsException(ByteWheelsException.Codes.CODE_UNPARSEABLE_DATE);
			}

			StringBuilder strVehicleQuery = new StringBuilder();
			SimpleDateFormat dtFrmt = new SimpleDateFormat("yyyy-MM-dd");
			String strStartDate = dtFrmt.format(fromDate);
			System.out.println(strStartDate);
			strVehicleQuery.append("SELECT CASE WHEN (vm.vehiclemodelcount - COUNT(b.vehicleid)) > 0 THEN v.vehicleid ELSE NULL END AS vehicleid ");
			strVehicleQuery.append("FROM vehicles v INNER JOIN vehiclecategories vc ON v.vehiclecategoryid = vc.vehiclecategoryid ");
			strVehicleQuery.append("INNER JOIN vehiclemodels vm ON v.vehiclemodelid = vm.vehiclemodelid ");
			strVehicleQuery.append("LEFT OUTER JOIN bookings b ON v.vehicleid = b.vehicleid ");
			strVehicleQuery.append("WHERE (b.enddate IS NULL OR b.enddate >= '").append(strStartDate).append("') ");
			strVehicleQuery.append("AND vc.vehiclecategoryid = ").append(categoryId);
			strVehicleQuery.append(" AND vm.vehiclemodelid = ").append(modelId);
			strVehicleQuery.append(" LIMIT 1;");
			System.out.println(strVehicleQuery.toString());

			Query query = this.entityManager.createNativeQuery(strVehicleQuery.toString());
			Object result = query.getSingleResult();
			if (null == result) {
				responseObject.put("err", true);
				responseObject.put("message", "No vehicles found.");
			} else {
				System.out.println("result:: " + result);
				Integer vehicleId = (Integer) result;
				Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
				VehicleModel vehiclemodel = entityManager.find(VehicleModel.class, modelId);
				VehicleCategory vehiclecategory = entityManager.find(VehicleCategory.class, categoryId);

				System.out.println("toDate:" + toDate);
				System.out.println("fromDate:" + fromDate);
				Long cost = vehiclecategory.getCategorycost() * (((toDate - fromDate) / (1000 * 60 * 60 * 24)) + 1);

				Booking objBooking = new Booking();
				objBooking.setEmailid(emailId);

				HashMap<String, String> bookingDetails = new HashMap<>();
				bookingDetails.put("name", emailId.split("@")[0]);
				bookingDetails.put("bookingID", String.valueOf("BI_" + new Random().nextInt(100000)));
				bookingDetails.put("vehicleModel", vehiclemodel.getVehiclemodelname());
				bookingDetails.put("vehicleNo", vehicle.getVehicleno());
				bookingDetails.put("fromDate", new Date(fromDate).toString());
				bookingDetails.put("toDate", new Date(toDate).toString());
				bookingDetails.put("totalCost", cost.toString());

				sendConfirmationMail(emailId, InvoiceTemplateGenerator.generateHtmlTemplate(bookingDetails));
				objBooking.setStartdate(new Date(fromDate));
				objBooking.setEnddate(new Date(toDate));
				objBooking.setVehicle(vehicle);

				entityManager.persist(objBooking);
				entityManager.flush();

				responseObject.put("err", false);
				responseObject.put("message", "Successfully booked.");
			}

		} catch (JSONException e) {
			logger.logError(null, e);
			throw new ByteWheelsException(ByteWheelsException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			throw new ByteWheelsException(ByteWheelsException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}
		return responseObject.toString();
	}

	private void sendConfirmationMail(String emailTo, String content) {

		String from = "do.not.reply.byte.wheels@gmail.com";
		String cc = "lokeshbayannagari92@gmail.com";
		String subject = "BYTE WHEELS BOOKING CONFIRMED!";
		String mimeType = "text/html";
		String strPassword = "bytewheelspassword";
		MailUtility.sendMail(from, emailTo, cc, subject, content, null, null, mimeType, strPassword);
	}
}
