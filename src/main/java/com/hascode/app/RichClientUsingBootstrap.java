package com.hascode.app;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RichClientUsingBootstrap {
	private static final File HTML_FILE = new File("app/main-with-layout.html");

	private Shell shell;
	private Display display;
	private Browser browser;
	private GridLayout shellLayout;
	private GridData browserLayout;

	public static void main(final String... args) {
		new RichClientUsingBootstrap().initializeApplication();
	}

	public void initializeApplication() {
		display = new Display();
		shell = new Shell(display);
		shell.setText("hasCode.com SWT HTML App");
		shell.setSize(800, 600);
		shellLayout = new GridLayout();
		shellLayout.numColumns = 2;
		shell.setLayout(shellLayout);

		browser = new Browser(shell, SWT.NONE);
		browserLayout = new GridData();
		browserLayout.verticalAlignment = GridData.FILL;
		browserLayout.horizontalAlignment = GridData.FILL;
		browserLayout.grabExcessHorizontalSpace = true;
		browserLayout.grabExcessVerticalSpace = true;
		browser.setLayoutData(browserLayout);
		browser.setJavascriptEnabled(true);

		browser.setUrl(HTML_FILE.toURI().toString());

		final BrowserFunction debugCallback = new CallbackDebugFunction(
				browser, "jsToSwtCallback");
		browser.addProgressListener(new ProgressAdapter() {
			@Override
			public void completed(final ProgressEvent event) {
				browser.addLocationListener(new LocationAdapter() {
					@Override
					public void changed(final LocationEvent event) {
						browser.removeLocationListener(this);
						debugCallback.dispose();
					}
				});
			}
		});
		System.out.println("browser initialized");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	static class CallbackDebugFunction extends BrowserFunction {
		public CallbackDebugFunction(final Browser browser, final String name) {
			super(browser, name);
		}

		@Override
		public Object function(final Object[] arguments) {
			System.out.println("callback triggered by js function");
			for (final Object o : arguments) {
				System.out.println("argument received: " + o.toString());
			}
			return "The date is: " + new Date().toString();
		}

	}

}
