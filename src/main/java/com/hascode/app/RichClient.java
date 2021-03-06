package com.hascode.app;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RichClient {
	private static final int APP_HEIGHT = 600;
	private static final int APP_WIDTH = 800;
	private static final String APP_TITLE = "hasCode.com SWT HTML App";
	private static final File HTML_FILE = new File("app/main.html");

	private Shell shell;
	private Display display;
	private Browser browser;
	private GridData browserLayout;

	public static void main(final String... args) {
		new RichClient().initializeApplication();
	}

	public void initializeApplication() {
		display = new Display();

		shell = new Shell(display);
		shell.setText(APP_TITLE);
		shell.setSize(APP_WIDTH, APP_HEIGHT);
		shell.setLayout(new GridLayout());

		browser = new Browser(shell, SWT.NONE);
		browserLayout = new GridData(GridData.FILL_BOTH);
		browserLayout.grabExcessHorizontalSpace = true;
		browserLayout.grabExcessVerticalSpace = true;
		browser.setLayoutData(browserLayout);

		browser.setUrl(HTML_FILE.toURI().toString());

		final BrowserFunction debugCallback = new CallbackDebugFunction(
				browser, "jsToSwtCallback");
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
