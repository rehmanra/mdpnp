package org.mdpnp.apps.testapp.dataqualitymonitor;

import java.io.IOException;
import java.net.URL;

import org.mdpnp.apps.fxbeans.NumericFxList;
import org.mdpnp.apps.fxbeans.SampleArrayFxList;
import org.mdpnp.apps.testapp.DeviceListModel;
import org.mdpnp.apps.testapp.IceApplicationProvider;
import org.mdpnp.rtiapi.data.EventLoop;
import org.springframework.context.ApplicationContext;

import com.rti.dds.subscription.Subscriber;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class DataQualityMonitorAppFactory implements IceApplicationProvider {

	private IceApplicationProvider.AppType type=new IceApplicationProvider.AppType(
			
			"Data Quality Monitor", "NoDataQualityMonitor", (URL) DataQualityMonitorAppFactory.class.getResource("dataqualitymonitorapp.png"), 0.75, false
		);

	public DataQualityMonitorAppFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public AppType getAppType() {
		return type;
	}

	@Override
	public IceApp create(ApplicationContext parentContext) throws IOException {
		final DeviceListModel deviceListModel = parentContext.getBean("deviceListModel", DeviceListModel.class);
		final NumericFxList numericList = parentContext.getBean("numericList", NumericFxList.class);
		final SampleArrayFxList sampleList = parentContext.getBean("sampleArrayList", SampleArrayFxList.class);
		final Subscriber subscriber = (Subscriber) parentContext.getBean("subscriber");
        final EventLoop eventLoop = (EventLoop) parentContext.getBean("eventLoop");
		FXMLLoader loader = new FXMLLoader(DataQualityMonitorApp.class.getResource("DataQualityMonitorApp.fxml"));

        final Parent ui = loader.load();
       
        final DataQualityMonitorApp controller = ((DataQualityMonitorApp) loader.getController());

        controller.set(parentContext, subscriber, deviceListModel, numericList, sampleList);
        controller.start(eventLoop, subscriber);
        
		return new IceApplicationProvider.IceApp() {
			
			@Override
			public void stop() throws Exception {
				controller.stop();
			}
			
			@Override
			public Parent getUI() {
				return ui;
			}
			
			@Override
			public AppType getDescriptor() {
				return getAppType();
			}
			
			@Override
			public void destroy() throws Exception {
				controller.destroy();
			}
			
			@Override
			public void activate(ApplicationContext context) {
				controller.activate();
			}
			
			@Override
			public int getPreferredWidth() {
				return 1570;
			}

			@Override
			public int getPreferredHeight() {
				return 400;
			}
		};
	}
}
