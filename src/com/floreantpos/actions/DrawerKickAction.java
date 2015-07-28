package com.floreantpos.actions;

import com.floreantpos.main.Application;
import com.floreantpos.model.UserPermission;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DrawerUtil;

public class DrawerKickAction extends PosAction {
	//String executableFileName = "drawer-kick.bat";
	
	public DrawerKickAction() {
		super("NO SALE", UserPermission.DRAWER_PULL); //$NON-NLS-1$
		
		setEnabled(Application.getInstance().getTerminal().isHasCashDrawer());
		
//		String osName = System.getProperty("os.name").toLowerCase();
//		if(osName.contains("linux")) {
//			executableFileName = "drawer-kick.sh";
//		}
	}

	@Override
	public void execute() {
		try {
//			File file = new File(Application.getInstance().getLocation(), executableFileName); //$NON-NLS-1$
//			if (file.exists()) {
//				Runtime.getRuntime().exec(file.getAbsolutePath());
//			}
			
			DrawerUtil.kickDrawer();
			
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(),e.getMessage(), e);
		}
	}

}
