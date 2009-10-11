package com.mdss.pos.main;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import com.mdss.pos.model.CookingInstruction;
import com.mdss.pos.model.CouponAndDiscount;
import com.mdss.pos.model.MenuCategory;
import com.mdss.pos.model.MenuGroup;
import com.mdss.pos.model.MenuItem;
import com.mdss.pos.model.MenuItemModifierGroup;
import com.mdss.pos.model.MenuModifier;
import com.mdss.pos.model.MenuModifierGroup;
import com.mdss.pos.model.PayoutReason;
import com.mdss.pos.model.PayoutRecepient;
import com.mdss.pos.model.Restaurant;
import com.mdss.pos.model.Shift;
import com.mdss.pos.model.Tax;
import com.mdss.pos.model.User;
import com.mdss.pos.model.VoidReason;
import com.mdss.pos.model.dao.CookingInstructionDAO;
import com.mdss.pos.model.dao.CouponAndDiscountDAO;
import com.mdss.pos.model.dao.GenericDAO;
import com.mdss.pos.util.ShiftUtil;

public class SchemaManager {

	static void update() throws Exception {
		Configuration configuration = new Configuration().configure();

		SchemaUpdate schemaUpdate = new SchemaUpdate(configuration);
		schemaUpdate.execute(true, true);

		GenericDAO dao = new GenericDAO();
		Session session = dao.getSession();

		User user = (User) session.get(User.class, "123");
		user.setPassword("123");
		session.saveOrUpdate(user);
		
		Shift shift = new Shift();
		Date start = ShiftUtil.buildShiftStartTime(6,0,Calendar.AM,2,0,Calendar.PM);
		Date end = ShiftUtil.buildShiftEndTime(2,0,Calendar.PM,10,0,Calendar.PM);
		shift.setName("MORNING");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);
		
		shift = new Shift();
		start = ShiftUtil.buildShiftStartTime(2,0,Calendar.PM,10,0,Calendar.PM);
		end = ShiftUtil.buildShiftEndTime(10,0,Calendar.PM,6,0,Calendar.AM);
		shift.setName("DAY");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);
		
		shift = new Shift();
		start = ShiftUtil.buildShiftStartTime(10,0,Calendar.PM,6,0,Calendar.AM);
		end = ShiftUtil.buildShiftEndTime(6,0,Calendar.AM,2,0,Calendar.PM);
		shift.setName("NIGHT");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);

	}

	static void insertData() {
		try {
			Configuration configuration = new Configuration().configure();

			SchemaExport schemaExport = new SchemaExport(configuration);
			schemaExport.create(true, true);

			GenericDAO dao = new GenericDAO();

			Restaurant restaurant = new Restaurant();
			restaurant.setId(1);
			restaurant.setName("Floreant Restaurant");
			restaurant.setAddressLine1("addressLine1");
			restaurant.setAddressLine2("addressLine2");
			dao.saveOrUpdate(restaurant);

			User user = new User();
			user.setUserId(123);
			user.setPassword("123");
			user.setFirstName("Manager");
			user.setLastName("1");
			dao.saveOrUpdate(user);

			Tax tax = new Tax();
			tax.setName("US");
			tax.setRate(new Double(10));
			dao.save(tax);

			PayoutReason payoutReason = new PayoutReason();
			payoutReason.setReason("SERVICE RENDRED");
			dao.save(payoutReason);

			payoutReason = new PayoutReason();
			payoutReason.setReason("PRODUCT DELIVERY");
			dao.save(payoutReason);

			payoutReason = new PayoutReason();
			payoutReason.setReason("REFUND");
			dao.save(payoutReason);

			payoutReason = new PayoutReason();
			payoutReason.setReason("PAY ADVANCE");
			dao.save(payoutReason);

			VoidReason voidReason = new VoidReason();
			voidReason.setReasonText("WRONG ITEM ORDERED");
			dao.save(voidReason);

			voidReason = new VoidReason();
			voidReason.setReasonText("CUSTOMER DIDN'T LIKE");
			dao.save(voidReason);

			PayoutRecepient recepient = new PayoutRecepient();
			recepient.setName("MISC");
			dao.save(recepient);

			recepient = new PayoutRecepient();
			recepient.setName("BAKERY");
			dao.save(recepient);

			MenuModifierGroup mg = new MenuModifierGroup();
			mg.setName("Bacons & Sausage");
			mg.setEnable(true);
			dao.save(mg);

			MenuModifierGroup mg2 = new MenuModifierGroup();
			mg2.setName("Drinks");
			mg2.setEnable(true);
			dao.save(mg2);

			MenuModifier m1 = new MenuModifier();
			m1.setName("Bacon");
			m1.setPrice(new Double(2));
			m1.setExtraPrice(0.5);
			m1.setTax(tax);
			m1.setModifierGroup(mg);
			m1.setEnable(true);
			dao.save(m1);

			MenuModifier m2 = new MenuModifier();
			m2.setName("Cheese Bacon");
			m2.setPrice(new Double(2));
			m2.setExtraPrice(0.5);
			m2.setTax(tax);
			m2.setModifierGroup(mg);
			m2.setEnable(true);
			dao.save(m2);

			MenuModifier m3 = new MenuModifier();
			m3.setName("Sausage");
			m3.setPrice(new Double(2));
			m3.setExtraPrice(0.5);
			m3.setTax(tax);
			m3.setModifierGroup(mg);
			m3.setEnable(true);
			dao.save(m3);

			MenuModifier m4 = new MenuModifier();
			m4.setName("Coke");
			m4.setPrice(new Double(10));
			m4.setExtraPrice(new Double(10));
			m4.setTax(tax);
			m4.setModifierGroup(mg2);
			m4.setEnable(true);
			dao.save(m4);

			MenuModifier m5 = new MenuModifier();
			m5.setName("Pepsi");
			m5.setPrice(new Double(10));
			m5.setExtraPrice(new Double(10));
			m5.setTax(tax);
			m5.setModifierGroup(mg2);
			m5.setEnable(true);
			dao.save(m5);

			MenuModifier m6 = new MenuModifier();
			m6.setName("Fanta");
			m6.setPrice(new Double(10));
			m6.setExtraPrice(new Double(10));
			m6.setTax(tax);
			m6.setModifierGroup(mg2);
			m6.setEnable(true);
			dao.save(m6);

			MenuCategory breakfast = new MenuCategory();
			breakfast.setName("BREAKFAST");
			breakfast.setVisible(true);
			dao.saveOrUpdate(breakfast);

			MenuCategory launch = new MenuCategory();
			launch.setName("LAUNCH");
			launch.setVisible(true);
			dao.saveOrUpdate(launch);

			MenuCategory dinner = new MenuCategory();
			dinner.setName("DINNER");
			dinner.setVisible(true);
			dao.saveOrUpdate(dinner);

			MenuGroup favourite = new MenuGroup();
			favourite.setName("FAVOURITE");
			favourite.setParent(breakfast);
			favourite.setVisible(true);
			dao.saveOrUpdate(favourite);

			MenuItemModifierGroup menuItemModifierGroup = new MenuItemModifierGroup();
			menuItemModifierGroup.setMinQuantity(1);
			menuItemModifierGroup.setMaxQuantity(2);
			menuItemModifierGroup.setModifierGroup(mg);

			MenuItem grandSlam = new MenuItem();
			grandSlam.setName("GRAND SLAM");
			grandSlam.setPrice(new Double(10));
			grandSlam.setTax(tax);
			grandSlam.setParent(favourite);
			grandSlam.addTomenuItemModiferGroups(menuItemModifierGroup);
			dao.save(grandSlam);

			MenuItem parata = new MenuItem();
			parata.setName("PLAIN OMLETE");
			parata.setPrice(new Double(10));
			parata.setDiscountRate(new Double(0));
			parata.setTax(tax);
			parata.setParent(favourite);
			dao.save(parata);

			MenuItem burger = new MenuItem();
			burger.setName("BURGER");
			burger.setPrice(new Double(20));
			burger.setDiscountRate(new Double(0));
			burger.setParent(favourite);
			dao.save(burger);

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			Date date = cal.getTime();

			CouponAndDiscountDAO couponDAO = new CouponAndDiscountDAO();

			CouponAndDiscount coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-1");
			coupon.setType(CouponAndDiscount.FIXED_PER_CATEGORY);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-2");
			coupon.setType(CouponAndDiscount.FIXED_PER_ITEM);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-3");
			coupon.setType(CouponAndDiscount.FIXED_PER_ORDER);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-4");
			coupon.setType(CouponAndDiscount.PERCENTAGE_PER_CATEGORY);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-5");
			coupon.setType(CouponAndDiscount.PERCENTAGE_PER_ITEM);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			coupon = new CouponAndDiscount();
			coupon.setName("CouponAndDiscount-6");
			coupon.setType(CouponAndDiscount.PERCENTAGE_PER_ORDER);
			coupon.setExpiryDate(date);
			coupon.setValue(new Double(10));
			couponDAO.save(coupon);

			CookingInstructionDAO cookingInstructionDAO = new CookingInstructionDAO();
			CookingInstruction cookingInstruction = new CookingInstruction();
			cookingInstruction.setDescription("WAIT 15 MINUTES");
			cookingInstructionDAO.save(cookingInstruction);

			cookingInstruction = new CookingInstruction();
			cookingInstruction.setDescription("SERVE HOT");
			cookingInstructionDAO.save(cookingInstruction);
			
			Shift shift = new Shift();
			Date start = ShiftUtil.buildShiftStartTime(6,0,Calendar.AM,2,0,Calendar.PM);
			Date end = ShiftUtil.buildShiftEndTime(2,0,Calendar.PM,10,0,Calendar.PM);
			shift.setName("MORNING");
			shift.setStartTime(start);
			shift.setEndTime(end);
			shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
			
			dao.saveOrUpdate(shift);
			
			shift = new Shift();
			start = ShiftUtil.buildShiftStartTime(2,0,Calendar.PM,10,0,Calendar.PM);
			end = ShiftUtil.buildShiftEndTime(10,0,Calendar.PM,6,0,Calendar.AM);
			shift.setName("DAY");
			shift.setStartTime(start);
			shift.setEndTime(end);
			shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
			
			dao.saveOrUpdate(shift);
			
			shift = new Shift();
			start = ShiftUtil.buildShiftStartTime(10,0,Calendar.PM,6,0,Calendar.AM);
			end = ShiftUtil.buildShiftEndTime(6,0,Calendar.AM,2,0,Calendar.PM);
			shift.setName("NIGHT");
			shift.setStartTime(start);
			shift.setEndTime(end);
			shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
			
			dao.saveOrUpdate(shift);

			//JOptionPane.showMessageDialog(null, "Successfully created database");
			System.out.println("Database create");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error has occured, could not create database");
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *@throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		//update();
		insertData();

	}

}
