package com.caro.smartmodule.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;


public class AppInfoUtil {

	private Context c;

	public AppInfoUtil(Context context) {

		this.c = context;
	}

	/**
	 * Get the App's PackageName
	 * 
	 * @return the App's PackageName
	 */

	public static  String getPackageName(Context context) {

		return context.getPackageName();
	}

	/**
	 * Get The App VersionCode
	 * 
	 * @param context
	 * @return the App VersionCode
	 */

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {

		}
		return verCode;
	}

	/**
	 * Get The App VersionName
	 * 
	 * @param context
	 * @return the App VersionName
	 */

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}
		return verName;

	}

	/**
	 * Get The AppName
	 * 
	 * @param context
	 * @return the AppName
	 */

	public static String getAppName(Context context) {
		String verName = context
				.getResources()
				.getText(
						context.getResources().getIdentifier("app_name",
								"string", context.getPackageName())).toString();
		return verName;
	}

	/**
	 * Get The AppIcon
	 * 
	 * @param context
	 * @return the AppIcon
	 */

	public static Drawable getAppIcon(Context context) {
		Drawable icon = null;
		try {
			icon = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).applicationInfo
					.loadIcon(context.getPackageManager());
		} catch (NameNotFoundException e) {

		}
		return icon;
	}

	/**
	 * Get The App FirstInstallTime(requires API level 9)
	 * 
	 * @param context
	 * @return the App FirstInstallTime
	 */

	public static long getFirstInstallTime(Context context) {
		long fit = 0;
		try {
			fit = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).firstInstallTime;
		} catch (NameNotFoundException e) {

		}
		return fit;

	}

	/**
	 * Get The App LastUpdateTime(requires API level 9)
	 * 
	 * @param context
	 * @return the App LastUpdateTime
	 */

	public static long getLastUpdateTime(Context context) {
		long fit = 0;
		try {
			fit = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).lastUpdateTime;
		} catch (NameNotFoundException e) {

		}
		return fit;

	}

	/**
	 * Get the App's All use Permission
	 * 
	 * @param packageName
	 * @return the use Permissions by ArrayList<MyPermission> permissions
	 */

	public ArrayList<MyPermission> getUsesPermission(String packageName) {
		ArrayList<MyPermission> myPerms = new ArrayList<MyPermission>();
		try {
			PackageManager packageManager = c.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, PackageManager.GET_PERMISSIONS);
			String[] usesPermissionsArray = packageInfo.requestedPermissions;

			for (int i = 0; i < usesPermissionsArray.length; i++) {
				MyPermission permi = new MyPermission();
				String usesPermissionName = usesPermissionsArray[i];
				permi.setPermissionName(usesPermissionName);
				System.out.println("usesPermissionName=" + usesPermissionName);
				PermissionInfo permissionInfo = packageManager
						.getPermissionInfo(usesPermissionName, 0);
				PermissionGroupInfo permissionGroupInfo = packageManager
						.getPermissionGroupInfo(permissionInfo.group, 0);
				permi.setPermissionGroup(permissionGroupInfo.loadLabel(
						packageManager).toString());
				System.out.println("permissionGroup="
						+ permissionGroupInfo.loadLabel(packageManager)
								.toString());
				String permissionLabel = permissionInfo.loadLabel(
						packageManager).toString();
				permi.setPermissionLabel(permissionLabel);
				System.out.println("permissionLabel=" + permissionLabel);
				String permissionDescription = permissionInfo.loadDescription(
						packageManager).toString();
				permi.setPermissionDescription(permissionDescription);
				System.out.println("permissionDescription="
						+ permissionDescription);
				System.out
						.println("===========================================");
				myPerms.add(permi);

			}
			return myPerms;
		} catch (Exception e) {
		}
		return myPerms;
	}

	/**
	 * Get the Device's ALL Third Apps
	 * 
	 * @return the Device's ALL Third Apps
	 */

	public String getAllThirdApp() {
		String result = "";
		List<PackageInfo> packages = c.getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				result += i.applicationInfo.loadLabel(c.getPackageManager())
						.toString() + ",";
			}
		}
		return result.substring(0, result.length() - 1);
	}

	public class MyPermission {
		private String permissionName;
		private String permissionGroup;
		private String permissionLabel;
		private String permissionDescription;

		public String getPermissionName() {
			return permissionName;
		}

		public void setPermissionName(String permissionName) {
			this.permissionName = permissionName;
		}

		public String getPermissionGroup() {
			return permissionGroup;
		}

		public void setPermissionGroup(String permissionGroup) {
			this.permissionGroup = permissionGroup;
		}

		public String getPermissionLabel() {
			return permissionLabel;
		}

		public void setPermissionLabel(String permissionLabel) {
			this.permissionLabel = permissionLabel;
		}

		public String getPermissionDescription() {
			return permissionDescription;
		}

		public void setPermissionDescription(String permissionDescription) {
			this.permissionDescription = permissionDescription;
		}

	}

	/**
	 * 通过包名获取应用程序的名称
	 * @param pk
	 * @return 返回包名所对应的应用程序的名称
	 */
	public static String getProgramNameByPackageName(Context pContext, String pk) {
		PackageManager pm = pContext.getPackageManager();
		try {
			return pm.getApplicationLabel(pm.getApplicationInfo(pk, PackageManager.GET_META_DATA))
					.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


}
