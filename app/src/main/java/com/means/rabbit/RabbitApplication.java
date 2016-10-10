package com.means.rabbit;

import java.util.Locale;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.GlobalCodeHandler;
import net.duohuo.dhroid.net.GlobalParams;
import net.duohuo.dhroid.net.cache.DaoHelper;
import net.duohuo.dhroid.util.UserLocation;
import net.duohuo.dhroid.util.UserLocation.OnLocationChanged;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.means.rabbit.api.API;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.views.NomalDialog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class RabbitApplication extends Application implements
		Thread.UncaughtExceptionHandler {

	private static RabbitApplication instance;

	public IDialog dialoger;

	public static ImageLoaderConfiguration imageconfig;

	String Baseurl;

	// 是否使用手机
	boolean isphone = true;

	public int langtype = 0;

	public static RabbitApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Thread.setDefaultUncaughtExceptionHandler(this);
		instance = this;
		Const.netadapter_page_no = "p";
		Const.netadapter_step = "step";
		Const.response_total = "totalcount";
		Const.response_data = "data";
		Const.netadapter_step_default = 10;
		Const.DATABASE_VERSION = 18;
		// Const.response_success = "status";
		// Const.response_msg = "info";
		// Const.response_result_status = "1";
		// Const.netadapter_no_more = "";

		// Const.postType = 2;
		IocContainer.getShare().initApplication(this);
		IocContainer.getShare().bind(RabbitValueFix.class).to(ValueFix.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(NomalDialog.class).to(IDialog.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(DaoHelper.class)
				.to(OrmLiteSqliteOpenHelper.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(KmlGlobalCodeHandler.class)

		.to(GlobalCodeHandler.class).scope(InstanceScope.SCOPE_SINGLETON);
		dialoger = IocContainer.getShare().get(IDialog.class);
		// CrashHandler.getInstance().init();

		imageconfig = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(400, 400)
				// default = device screen dimensions
				.diskCacheExtraOptions(400, 400, null)
				.threadPoolSize(5)
				// default Thread.NORM_PRIORITY - 1
				.threadPriority(Thread.NORM_PRIORITY)
				// default FIFO
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13)
				// default
				.diskCache(
						new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
								this, true)))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// default
				.imageDownloader(new BaseImageDownloader(this))
				// default

				.imageDecoder(new BaseImageDecoder(false))
				// default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		ImageLoader.getInstance().init(imageconfig);

		GlobalParams globalParams = IocContainer.getShare().get(
				GlobalParams.class);
		globalParams.setGlobalParam("lang", "cn");

		RabbitPerference per = IocContainer.getShare().get(
				RabbitPerference.class);
		per.load();
		if (!TextUtils.isEmpty(per.catid)) {
			globalParams.setGlobalParam("cityid", per.catid);
		}

		if (per.isFirst != 0) {
			UserLocation location = UserLocation.getInstance();
			location.init(this);
		}

		Resources resources = this.getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
		if (per.langType != 0) {
			if (per.langType == 1) {
				config.locale = Locale.CHINA; // 简体中文
				isphone = true;
				setBaseUrl(1);
			} else if (per.langType == 2) {
				config.locale = Locale.ENGLISH; // 简体中文
				isphone = false;
				setBaseUrl(2);
			} else if (per.langType == 3) {
				config.locale = new Locale("my");
				isphone = false;
				setBaseUrl(3);
			}
			resources.updateConfiguration(config, dm);

		} else {
			if (config.locale == Locale.ENGLISH) {
				setBaseUrl(2);
				isphone = false;
			} else if (config.locale == new Locale("my")) {
				setBaseUrl(3);
				isphone = false;
			} else {
				setBaseUrl(1);
				isphone = true;
			}
		}

	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// System.out.println("濂旀簝.................");
		// Intent intent = new Intent(this, SplashActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void setIsPhone(boolean isphone) {
		this.isphone = isphone;
	}

	public String getBaseUrl() {
		return Baseurl;
	}

	public boolean getisPhone() {
		return isphone;
	}

	public int getLangType() {
		return langtype;
	}

	public void setBaseUrl(int type) {
		switch (type) {
		case 1:
			Baseurl = "http://cn.lazybunny.c.wanruankeji.com";
			langtype = 1;
			break;

		case 2:
			Baseurl = "http://en.lazybunny.c.wanruankeji.com";
			langtype = 2;
			break;

		case 3:
			Baseurl = "http://malaysia.lazybunny.c.wanruankeji.com";
			langtype = 3;
			break;

		default:
			break;
		}

	}

}
