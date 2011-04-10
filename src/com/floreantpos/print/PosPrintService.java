package com.floreantpos.print;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.config.PrintConfig;
import com.floreantpos.jreports.JReportPrintService;
import com.floreantpos.main.Application;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.DrawerPullVoidTicketEntry;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCookingInstruction;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.TipsCashoutReport.TipsCashoutReportData;
import com.floreantpos.model.dao.RestaurantDAO;

import foxtrot.Job;
import foxtrot.Worker;

public class PosPrintService {
	private static Log logger = LogFactory.getLog(PosPrintService.class);
	
	static int firstColumnLength = 4;
	static int secondColumnLength = 16;
	static int thirdColumnLength = 8;
	static int fourthColumnLength = 8;
	static int columnGap = 2;
	static int totalLength = 42;

	static int kitchenFirstColumnLength = 4;
	static int kitchenSecondColumnLength = 24;
	static int kitchenThirdColumnLength = 8;

	static void printCentered(PosPrinter printer, String text) {
		int blank = totalLength - text.length();
		int half = blank / 2;

		text = StringUtils.leftPad(text, half + text.length(), ' ');
		text = StringUtils.rightPad(text, totalLength, ' ');

		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText(text);
		printer.endLine();
	}

	static void printSeparator(PosPrinter printer, char separatorChar) {
		String text = String.valueOf(separatorChar);
		text = StringUtils.leftPad(text, totalLength, separatorChar);

		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText(text);
		printer.endLine();

	}

	static void print1stColumn(PosPrinter printer, String text, int columnLength) {
		printer.beginLine(PosPrinter.SIZE_0);
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printLastColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
		printer.endLine();
	}

	static void printColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printRightAlignedColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.leftPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printColumnSeparator(PosPrinter printer) {
		printer.printText("  ");
	}

	static void printMultilineColumn(PosPrinter printer, String text, int previoisColumnLength, int columnLength, boolean padLeft) {
		if (text.length() < columnLength) {
			if (padLeft) {
				text = StringUtils.leftPad(text, text.length() + previoisColumnLength + columnGap, ' ');
				text = StringUtils.rightPad(text, columnLength + previoisColumnLength + columnGap, ' ');
			}
			else {
				text = StringUtils.rightPad(text, columnLength, ' ');
			}
		}
		else if (text.length() > columnLength) {
			String stringBefore = text.substring(0, columnLength);
			String stringAfter = text.substring(columnLength);

			if (padLeft) {
				stringBefore = StringUtils.leftPad(stringBefore, previoisColumnLength + columnGap, ' ');
			}

			printer.printText(stringBefore);
			printer.endLine();
			printer.beginLine(PosPrinter.SIZE_0);
			printSecondColumn(printer, stringAfter, previoisColumnLength, columnLength, true);

			return;
		}

		printer.printText(text);
	}

	static void printFirstColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0 + "  ");
	}

	static void printSecondColumn(PosPrinter printer, String text, int firstColumnLength, int secondColumnLength, boolean padLeft) {
		if (text.length() < secondColumnLength) {
			if (padLeft) {
				text = StringUtils.leftPad(text, text.length() + firstColumnLength + columnGap, ' ');
				text = StringUtils.rightPad(text, secondColumnLength + firstColumnLength + columnGap, ' ');
			}
			else {
				text = StringUtils.rightPad(text, secondColumnLength, ' ');
			}
		}
		else if (text.length() > secondColumnLength) {
			String stringBefore = text.substring(0, secondColumnLength);
			String stringAfter = text.substring(secondColumnLength);

			if (padLeft) {
				stringBefore = StringUtils.leftPad(stringBefore, firstColumnLength + columnGap, ' ');
			}

			printer.printText(stringBefore);
			printer.endLine();
			printer.beginLine(PosPrinter.SIZE_0);
			printSecondColumn(printer, stringAfter, firstColumnLength, secondColumnLength, true);

			return;
		}

		printer.printText(text);
		printer.printText("  ");
	}

	static void printThirdColumn(PosPrinter printer, String text, int thirdColumnLength) {
		if (text.length() < thirdColumnLength) {
			text = StringUtils.leftPad(text, thirdColumnLength, ' ');
		}
		printer.printText(text);
		printer.printText("  ");
	}

	static void printFourthColumn(PosPrinter printer, String text, int fourthColumnLength) {
		if (text.length() < fourthColumnLength) {
			text = StringUtils.leftPad(text, fourthColumnLength, ' ');
		}
		printer.printText(text);
	}

	public static void printTicket(final Ticket ticket) throws Exception {
		Job job = new Job() {

			@Override
			public Object run() {
				PosPrinter posPrinter = null;
				try {
					if(PrintConfig.getReceiptPrinterType() == PrinterType.OS_PRINTER) {
						JReportPrintService.printTicket(ticket);
						return null;
					}
				
					Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

					posPrinter = new PosPrinter(PrintConfig.getJavaPosReceiptPrinterName(), PrintConfig.getCashDrawerName());

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText("\u001b|cA\u001b|2C" + restaurant.getName());
					posPrinter.endLine();

					if (restaurant.getAddressLine1() != null) {
						printCentered(posPrinter, restaurant.getAddressLine1());
					}
					if (restaurant.getAddressLine2() != null) {
						printCentered(posPrinter, restaurant.getAddressLine2());
					}
					if (restaurant.getAddressLine3() != null) {
						printCentered(posPrinter, restaurant.getAddressLine3());
					}
					if (restaurant.getTelephone() != null) {
						printCentered(posPrinter, restaurant.getTelephone());
					}

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.CHK_NO);
					posPrinter.printText(String.valueOf(ticket.getId()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.TBL_);
					posPrinter.printText(String.valueOf(ticket.getTableNumber()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.GUEST + " #");
					posPrinter.printText(String.valueOf(ticket.getNumberOfGuests()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.SRV_);
					posPrinter.printText(String.valueOf(ticket.getOwner().getUserId() + "/" + ticket.getOwner()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.DATE + ": ");
					posPrinter.printText(Application.formatDate(new Date()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					printFirstColumn(posPrinter, com.floreantpos.POSConstants.QTY, firstColumnLength);
					printSecondColumn(posPrinter, com.floreantpos.POSConstants.ITEM, firstColumnLength, secondColumnLength, false);
					printThirdColumn(posPrinter, com.floreantpos.POSConstants.UPRICE, thirdColumnLength);
					printFourthColumn(posPrinter, com.floreantpos.POSConstants.SUBTOTAL, fourthColumnLength);
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("", totalLength, "_"));
					posPrinter.endLine();

					List<TicketItem> ticketItems = ticket.getTicketItems();
					if (ticketItems != null) {
						for (TicketItem ticketItem : ticketItems) {
							posPrinter.beginLine(PosPrinter.SIZE_0);
							printFirstColumn(posPrinter, String.valueOf(ticketItem.getItemCount()), firstColumnLength);

							printSecondColumn(posPrinter, ticketItem.getName(), firstColumnLength, secondColumnLength, false);

							printThirdColumn(posPrinter, Application.formatNumber(ticketItem.getUnitPrice()), thirdColumnLength);

							printFourthColumn(posPrinter, Application.formatNumber(ticketItem.getSubtotalAmountWithoutModifiers()), fourthColumnLength);

							posPrinter.endLine();

							List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
							if (modifierGroups != null) {
								for (TicketItemModifierGroup modifierGroup : modifierGroups) {
									List<TicketItemModifier> modifiers = modifierGroup.getTicketItemModifiers();
									if (modifiers != null) {
										for (TicketItemModifier modifier : modifiers) {
											if (modifier.getTotalAmount() == 0) {
												continue;
											}
											boolean extra = false;
											String display = " - " + modifier.getName();
											if (modifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
												display = " - Extra " + display;
												extra = true;
											}

											posPrinter.beginLine(PosPrinter.SIZE_0);
											printFirstColumn(posPrinter, String.valueOf(modifier.getItemCount()), firstColumnLength);

											printSecondColumn(posPrinter, display, firstColumnLength, secondColumnLength, false);
											if(extra) {
												printThirdColumn(posPrinter, Application.formatNumber(modifier.getExtraUnitPrice()), thirdColumnLength);
											}
											else {
												printThirdColumn(posPrinter, Application.formatNumber(modifier.getUnitPrice()), thirdColumnLength);
											}

											printFourthColumn(posPrinter, Application.formatNumber(modifier.getTotalAmount()), fourthColumnLength);

											posPrinter.endLine();
										}
									}
								}
							}

						}
					}

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("", totalLength, "_"));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("SUB-TOTAL  " + ":", 32));
					posPrinter.printText(StringUtils.leftPad(Application.formatNumber(ticket.getSubtotalAmount()), 10));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("TAX        " + ":", 32));
					posPrinter.printText(StringUtils.leftPad(Application.formatNumber(ticket.getTaxAmount()), 10));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("GRAND TOTAL " + ":", 32));
					posPrinter.printText(StringUtils.leftPad(Application.formatNumber(ticket.getTotalAmount()), 10));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("TIP " + ":", 32));
					if (ticket.getGratuity() != null) {
						posPrinter.printText(StringUtils.leftPad(Application.formatNumber(ticket.getGratuity().getAmount()), 10));
					}
					posPrinter.endLine();
					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(StringUtils.leftPad("TOTAL " + ":", 32));
					posPrinter.endLine();

					printCentered(posPrinter, "THANK YOU!!");
					printCentered(posPrinter, "PLEASE COME AGAIN!!!");

					posPrinter.printCutPartial();
					//posPrinter.openDrawer();
				} catch(Exception x) {
					logger.error("Error while printing ticket", x);
				} finally {
					if (posPrinter != null) {
						posPrinter.finalize();
					}
				}
				return null;
			}
		};
		
		Worker.post(job);
	}
	
	public static void printToKitchen(final Ticket ticket) throws Exception {
		Job job = new Job() {

			@Override
			public Object run() {
				PosPrinter posPrinter = null;
				try {
					if(PrintConfig.getKitchenPrinterType() == PrinterType.OS_PRINTER) {
						JReportPrintService.printTicketToKitchen(ticket);
						return null;
					}
				
					Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

					posPrinter = new PosPrinter(PrintConfig.getJavaPosKitchenPrinterName(), PrintConfig.getCashDrawerName());

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText("\u001b|cA\u001b|2C" + restaurant.getName());
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText("Ticket #" + ticket.getId());
					posPrinter.endLine();
					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.SRV_);
					posPrinter.printText(String.valueOf(ticket.getOwner().getUserId() + "/" + ticket.getOwner()));
					posPrinter.endLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText(com.floreantpos.POSConstants.DATE + ": ");
					posPrinter.printText(Application.formatDate(new Date()));
					posPrinter.endLine();

					posPrinter.printEmptyLine();

					posPrinter.beginLine(PosPrinter.SIZE_0);
					printFirstColumn(posPrinter, "ITEM#", kitchenFirstColumnLength);
					printSecondColumn(posPrinter, "ITEM NAME", kitchenFirstColumnLength, kitchenSecondColumnLength, false);
					printFourthColumn(posPrinter, "UNIT", kitchenThirdColumnLength);
					posPrinter.endLine();

					List<TicketItem> ticketItems = ticket.getTicketItems();
					for (TicketItem ticketItem : ticketItems) {
						if (ticketItem.isShouldPrintToKitchen() && !ticketItem.isPrintedToKitchen()) {
							printItemToKitchen(posPrinter, ticketItem);
							ticketItem.setPrintedToKitchen(true);
						}

						List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
						if (ticketItemModifierGroups != null) {
							for (TicketItemModifierGroup modifierGroup : ticketItemModifierGroups) {
								List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
								if (ticketItemModifiers != null) {
									for (TicketItemModifier modifier : ticketItemModifiers) {
										if (modifier.isShouldPrintToKitchen() && !modifier.isPrintedToKitchen()) {
											printModifierToKitchen(posPrinter, modifier);
											modifier.setPrintedToKitchen(true);
										}
									}
								}
							}
						}
					}

					posPrinter.printEmptyLine();

					if (ticket.getDeletedItems() != null) {
						List deletedItems = ticket.getDeletedItems();
						for (Object object : deletedItems) {
							if (object instanceof TicketItem) {
								TicketItem item = (TicketItem) object;
								if (item.isShouldPrintToKitchen()) {
									printDeletedItem(posPrinter, item.getId());
								}
							}
							else if (object instanceof TicketItemModifier) {
								TicketItemModifier ticketItemModifier = (TicketItemModifier) object;
								if (ticketItemModifier.isShouldPrintToKitchen()) {
									printDeletedItem(posPrinter, ticketItemModifier.getId());
								}
							}
						}
					}

					posPrinter.beginLine(PosPrinter.SIZE_0);
					posPrinter.printText("\u001b|cA\u001b|2CINSTRUCTIONS");
					posPrinter.endLine();

					Set<TicketCookingInstruction> cookingInstructions = ticket.getCookingInstructions();
					if (cookingInstructions != null) {
						for (TicketCookingInstruction instruction : cookingInstructions) {
							if (!instruction.isPrintedToKitchen()) {
								printCentered(posPrinter, instruction.getDescription());
								instruction.setPrintedToKitchen(true);
							}
						}
					}
					posPrinter.printCutPartial();
				} catch(Exception x) {
					logger.error("Error while printing to kitchen", x);
				} finally {
					if (posPrinter != null) {
						posPrinter.finalize();
					}
				}
				return null;
			}
			
		};
		
		Worker.post(job);
	}

	public static void printVoidInfo(Ticket ticket) throws Exception {
		PosPrinter posPrinter = null;
		try {
			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

			posPrinter = new PosPrinter(PrintConfig.getJavaPosKitchenPrinterName(), PrintConfig.getCashDrawerName());

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("\u001b|cA\u001b|2C" + restaurant.getName());
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("\u001b|cA\u001DB\1============VOIDED CHECK============\u001DB\0");
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Ticket #" + ticket.getId());
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText(com.floreantpos.POSConstants.SRV_);
			posPrinter.printText(String.valueOf(ticket.getOwner().getUserId() + "/" + ticket.getOwner()));
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText(com.floreantpos.POSConstants.DATE + ": ");
			posPrinter.printText(Application.formatDate(new Date()));
			posPrinter.endLine();

			posPrinter.printEmptyLine();

			posPrinter.printCutPartial();
		} finally {
			if (posPrinter != null) {
				posPrinter.finalize();
			}
		}
	}

	private static void printDeletedItem(PosPrinter posPrinter, int itemId) {
		posPrinter.beginLine(PosPrinter.SIZE_0);
		printFirstColumn(posPrinter, "DO NOT COOK " + itemId, firstColumnLength);
		posPrinter.endLine();
	}

	private static void printItemToKitchen(PosPrinter posPrinter, TicketItem ticketItem) {
		posPrinter.beginLine(PosPrinter.SIZE_0);
		printFirstColumn(posPrinter, String.valueOf(ticketItem.getId()), kitchenFirstColumnLength);
		printSecondColumn(posPrinter, ticketItem.getName(), kitchenFirstColumnLength, kitchenSecondColumnLength, false);
		printFourthColumn(posPrinter, String.valueOf(ticketItem.getItemCount()), kitchenThirdColumnLength);
		posPrinter.endLine();
	}

	private static void printModifierToKitchen(PosPrinter posPrinter, TicketItemModifier modifier) {
		posPrinter.beginLine(PosPrinter.SIZE_0);
		printFirstColumn(posPrinter, String.valueOf(modifier.getId()), kitchenFirstColumnLength);
		printSecondColumn(posPrinter, " - " + modifier.getName(), kitchenFirstColumnLength, kitchenSecondColumnLength, false);
		printFourthColumn(posPrinter, String.valueOf(modifier.getItemCount()), kitchenThirdColumnLength);
		posPrinter.endLine();
	}

	static void printDrawerPullLine(PosPrinter printer, String firstColumn, String secondColumn) {
		printer.beginLine(PosPrinter.SIZE_0);
		printFirstColumn(printer, firstColumn, 30);
		printFourthColumn(printer, secondColumn, 10);
		printer.endLine();
	}

	static void printDiscountLine(PosPrinter printer, String firstColumn, String secondColumn) {
		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText("     ");
		printColumn(printer, firstColumn, 27);
		printRightAlignedColumn(printer, secondColumn, 10);
		printer.endLine();
	}

	public static void printDrawerPullReport(DrawerPullReport drawerPullReport, Terminal terminal) {
		PosPrinter posPrinter = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		try {
			posPrinter = new PosPrinter(PrintConfig.getJavaPosReceiptPrinterName(), PrintConfig.getCashDrawerName());

			printSeparator(posPrinter, '=');
			printCentered(posPrinter, "Time: " + Application.formatDate(drawerPullReport.getReportTime()));
			printSeparator(posPrinter, '=');

			posPrinter.printEmptyLine();

			printDrawerPullLine(posPrinter, " NET SALES", decimalFormat.format(drawerPullReport.getNetSales()));
			printDrawerPullLine(posPrinter, "+SALES TAX", decimalFormat.format(drawerPullReport.getSalesTax()));
			printDrawerPullLine(posPrinter, "=TOTAL REVENUES", decimalFormat.format(drawerPullReport.getTotalRevenue()));
			printDrawerPullLine(posPrinter, "+CHARGED TIPS", decimalFormat.format(drawerPullReport.getChargedTips()));
			printSeparator(posPrinter, '-');
			printDrawerPullLine(posPrinter, "=GROSS RECEIPTS", decimalFormat.format(drawerPullReport.getGrossReceipts()));
			posPrinter.printEmptyLine();
			printDrawerPullLine(posPrinter, "-CASH RECEIPTS (" + drawerPullReport.getCashReceiptNumber() + ")", decimalFormat.format(drawerPullReport.getCashReceiptAmount()));
			printDrawerPullLine(posPrinter, "-CREDIT CARDS  (" + drawerPullReport.getCreditCardReceiptNumber() + ")", decimalFormat.format(drawerPullReport.getCreditCardReceiptAmount()));
			printDrawerPullLine(posPrinter, "-DEBIT CARDS   (" + drawerPullReport.getDebitCardReceiptNumber() + ")", decimalFormat.format(drawerPullReport.getDebitCardReceiptAmount()));
			printDrawerPullLine(posPrinter, "-GIFT RETURNS   (" + drawerPullReport.getGiftCertReturnCount() + ")", decimalFormat.format(drawerPullReport.getGiftCertReturnAmount()));
			printDrawerPullLine(posPrinter, "+GIFT CERT. CHANGE" , decimalFormat.format(drawerPullReport.getGiftCertChangeAmount()));
			printDrawerPullLine(posPrinter, "+CASH BACK", decimalFormat.format(drawerPullReport.getCashBack()));
			printSeparator(posPrinter, '-');
			printDrawerPullLine(posPrinter, "=RECEIPT DIFFERENTIAL", decimalFormat.format(drawerPullReport.getReceiptDifferential()));
			posPrinter.printEmptyLine();
			printDrawerPullLine(posPrinter, "+CHARGED TIPS", decimalFormat.format(drawerPullReport.getChargedTips()));
			printDrawerPullLine(posPrinter, "-TIPS PAID", decimalFormat.format(drawerPullReport.getTipsPaid()));
			printSeparator(posPrinter, '-');
			printDrawerPullLine(posPrinter, "=TIPS DIFFERENTIAL", decimalFormat.format(drawerPullReport.getTipsDifferential()));
			posPrinter.printEmptyLine();

			printCentered(posPrinter, "CASH BALANCE");
			printSeparator(posPrinter, '=');
			printDrawerPullLine(posPrinter, "CASH  (" + drawerPullReport.getCashReceiptNumber() + ")", decimalFormat.format(drawerPullReport.getCashReceiptAmount()));
			printDrawerPullLine(posPrinter, "-TIPS PAID", decimalFormat.format(drawerPullReport.getTipsPaid()));
			printDrawerPullLine(posPrinter, "-PAY OUT       (" + drawerPullReport.getPayOutNumber() + ")", decimalFormat.format(drawerPullReport.getPayOutAmount()));
			printDrawerPullLine(posPrinter, "-CASH BACK", decimalFormat.format(drawerPullReport.getCashBack()));
			printDrawerPullLine(posPrinter, "+BEGIN CASH", decimalFormat.format(terminal.getOpeningBalance()));
			printDrawerPullLine(posPrinter, "-DRAWER BLEED  (" + drawerPullReport.getDrawerBleedNumber() + ")", decimalFormat.format(drawerPullReport.getDrawerBleedAmount()));
			printSeparator(posPrinter, '-');
			printDrawerPullLine(posPrinter, "=DRAWER ACCOUNTABLE", decimalFormat.format(drawerPullReport.getDrawerAccountable()));
			printDrawerPullLine(posPrinter, ">CASH TO DEPOSIT", decimalFormat.format(terminal.getCurrentBalance()));

			printCentered(posPrinter, "=== EXCEPTIONS ===");
			printCentered(posPrinter, "=== VOIDS/REFUNDS (Without Tax) ===");
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printColumn(posPrinter, "CODE", 6);
			printColumnSeparator(posPrinter);
			printColumn(posPrinter, "REASON", 10);
			printColumnSeparator(posPrinter);
			printColumn(posPrinter, "WAST", 4);
			printColumnSeparator(posPrinter);
			printColumn(posPrinter, com.floreantpos.POSConstants.QTY, 6);
			printColumnSeparator(posPrinter);
			printColumn(posPrinter, "AMOUNT", 7);
			posPrinter.endLine();
			
			Set<DrawerPullVoidTicketEntry> voidTickets = drawerPullReport.getVoidTickets();
			if (voidTickets != null) {
				for (DrawerPullVoidTicketEntry entry : voidTickets) {
					print1stColumn(posPrinter, String.valueOf(entry.getCode()), 6);
					printColumnSeparator(posPrinter);
					printColumn(posPrinter, entry.getReason(), 10);
					printColumnSeparator(posPrinter);
					printColumn(posPrinter, " ", 4);
					printColumnSeparator(posPrinter);
					printColumn(posPrinter, String.valueOf(entry.getQuantity()), 6);
					printColumnSeparator(posPrinter);
					printColumn(posPrinter, Application.formatNumber(entry.getAmount()), 7);
				}
			}
			printSeparator(posPrinter, '=');
			print1stColumn(posPrinter, "TOTAL VOIDS W/WST", 25);
			printColumnSeparator(posPrinter);
			printRightAlignedColumn(posPrinter, decimalFormat.format(drawerPullReport.getTotalVoidWst()), 15);
			print1stColumn(posPrinter, "TOTAL VOIDS", 25);
			printColumnSeparator(posPrinter);
			printRightAlignedColumn(posPrinter, decimalFormat.format(drawerPullReport.getTotalVoid()), 15);
			posPrinter.endLine();
			
			posPrinter.printEmptyLine();
			printLastColumn(posPrinter, "TOTAL DISCOUNT", 25);
			printDiscountLine(posPrinter, "TOTAL COUNT", String.valueOf(drawerPullReport.getTotalDiscountCount()));
			printDiscountLine(posPrinter, "TOTAL Dsct", Application.formatNumber(drawerPullReport.getTotalDiscountAmount()));
			printDiscountLine(posPrinter, "TOTAL Sales", Application.formatNumber(drawerPullReport.getTotalDiscountSales()));
			printDiscountLine(posPrinter, "TOTAL Guest", String.valueOf(drawerPullReport.getTotalDiscountGuest()));
			printDiscountLine(posPrinter, "Party Size", String.valueOf(drawerPullReport.getTotalDiscountPartySize()));
			printDiscountLine(posPrinter, "Check Size", String.valueOf(drawerPullReport.getTotalDiscountCheckSize()));
			printDiscountLine(posPrinter, "Count [%]", String.valueOf(" "));
			printDiscountLine(posPrinter, "Ration", String.valueOf(" "));
			
			posPrinter.printCutPartial();
		} finally {
			if (posPrinter != null) {
				posPrinter.finalize();
			}
		}
	}

	public static void printServerTipsReport(TipsCashoutReport report) {
		PosPrinter posPrinter = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		try {
			posPrinter = new PosPrinter(PrintConfig.getJavaPosReceiptPrinterName(), PrintConfig.getCashDrawerName());

			int c1 = 15;
			int c2 = 20;

			printCentered(posPrinter, "TIPS CASH OUT REPORT");
			printSeparator(posPrinter, '=');

			posPrinter.printEmptyLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Server", c1);
			printSecondColumn(posPrinter, ": " + report.getServer(), c1, c2, false);
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "From", c1);
			printSecondColumn(posPrinter, ": " + Application.formatDate(report.getFromDate()), c1, c2, false);
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "To", c1);
			printSecondColumn(posPrinter, ": " + Application.formatDate(report.getToDate()), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Time", c1);
			printSecondColumn(posPrinter, ": " + Application.formatDate(report.getReportTime()), c1, c2, false);
			posPrinter.endLine();

			List<TipsCashoutReportData> datas = report.getDatas();
			if (datas == null) {
				return;
			}

			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "REF#", firstColumnLength);
			printSecondColumn(posPrinter, "C/Type", firstColumnLength, secondColumnLength, false);
			printThirdColumn(posPrinter, "Total", thirdColumnLength);
			printFourthColumn(posPrinter, "Tips", fourthColumnLength);
			posPrinter.endLine();

			for (TipsCashoutReportData data : datas) {
				posPrinter.beginLine(PosPrinter.SIZE_0);
				printFirstColumn(posPrinter, String.valueOf(data.getTicketId()), firstColumnLength);
				printSecondColumn(posPrinter, data.getSaleType(), firstColumnLength, secondColumnLength, false);
				printThirdColumn(posPrinter, decimalFormat.format(data.getTicketTotal()), thirdColumnLength);
				printFourthColumn(posPrinter, decimalFormat.format(data.getTips()), fourthColumnLength);
				posPrinter.endLine();
			}

			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Transaction Count", c1);
			printSecondColumn(posPrinter, ": " + (report.getDatas() == null ? "0" : String.valueOf(report.getDatas().size())), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Cash Tips", c1);
			printSecondColumn(posPrinter, ": " + Application.formatNumber(report.getCashTipsAmount()), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Charged Tips", c1);
			printSecondColumn(posPrinter, ": " + Application.formatNumber(report.getChargedTipsAmount()), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Total Tips", c1);
			printSecondColumn(posPrinter, ": " + Application.formatNumber(report.getTotalTips()), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Average Tips", c1);
			printSecondColumn(posPrinter, ": " + Application.formatNumber(report.getAverageTips()), c1, c2, false);
			posPrinter.endLine();
			posPrinter.beginLine(PosPrinter.SIZE_0);
			printFirstColumn(posPrinter, "Tips Due", c1);
			printSecondColumn(posPrinter, ": " + Application.formatNumber(report.getTipsDue()), c1, c2, false);
			posPrinter.endLine();

			posPrinter.printCutPartial();
		} finally {
			if (posPrinter != null) {
				posPrinter.finalize();
			}
		}
	}

	/*
	public static void printMoneyReceipt(Ticket ticket) {
		PosPrinter posPrinter = null;
		try {
			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

			posPrinter = new PosPrinter(PrintConfig.getJavaPosReceiptPrinterName(), PrintConfig.getCashDrawerName());

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("\u001b|cA\u001b|2C" + restaurant.getName());
			posPrinter.endLine();

			if (restaurant.getAddressLine1() != null) {
				printCentered(posPrinter, restaurant.getAddressLine1());
			}
			if (restaurant.getAddressLine2() != null) {
				printCentered(posPrinter, restaurant.getAddressLine2());
			}
			if (restaurant.getAddressLine3() != null) {
				printCentered(posPrinter, restaurant.getAddressLine3());
			}
			if (restaurant.getTelephone() != null) {
				printCentered(posPrinter, restaurant.getTelephone());
			}

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.endLine();

			printCentered(posPrinter, "*** CHARGED RECEIPT ***");

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Date/Time: ");
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy hh:mm a");
			posPrinter.printText(dateFormat.format(new Date()));
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Check No: ");
			if(ticket.getCardNumber() != null) {
				posPrinter.printText(ticket.getCardNumber());
			}
			else {
				posPrinter.printText("Cash");
			}
			
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Server: ");
			posPrinter.printText(String.valueOf(ticket.getOwner().getUserId() + "/" + ticket.getOwner()));
			posPrinter.endLine();

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Reference: ");
			posPrinter.printText(String.valueOf(ticket.getTableNumber()));
			posPrinter.endLine();

			if (ticket.getCardType() != null) {
				posPrinter.beginLine(PosPrinter.SIZE_0);
				posPrinter.printText("Card Type: ");
				posPrinter.printText(String.valueOf(ticket.getCardType()));
				posPrinter.endLine();
			}

			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Subtotal: ");
			posPrinter.printText(Application.formatNumber(ticket.getSubtotalAmount()));
			posPrinter.endLine();
			
			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Discount: ");
			posPrinter.printText(Application.formatNumber(ticket.getDiscountAmount()));
			posPrinter.endLine();
			
			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Tax: ");
			posPrinter.printText(Application.formatNumber(ticket.getTaxAmount()));
			posPrinter.endLine();

			double totalAmount = ticket.getTotalAmount();
			if (ticket.getGratuity() != null) {
				posPrinter.beginLine(PosPrinter.SIZE_0);
				posPrinter.printText("Tip: ");
				posPrinter.printText(Application.formatNumber(ticket.getGratuity().getAmount()));
				posPrinter.endLine();
				
				totalAmount += ticket.getGratuity().getAmount();
			}
			posPrinter.beginLine(PosPrinter.SIZE_0);
			posPrinter.printText("Total: ");
			posPrinter.printText(Application.formatNumber(totalAmount));
			posPrinter.endLine();
			posPrinter.printCutPartial();
		} finally {
			if (posPrinter != null) {
				posPrinter.finalize();
			}
		}
	}*/
}
