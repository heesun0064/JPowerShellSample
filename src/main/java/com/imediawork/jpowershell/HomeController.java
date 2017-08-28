package com.imediawork.jpowershell;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController
{

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model)
	{
		// Execute a command in PowerShell session
		PowerShellResponse response = PowerShell.executeSingleCommand("Get-Process");

		// Print results
		String result = response.getCommandOutput();

		model.addAttribute("commandOutput", result);

		return "home";
	}

	@RequestMapping(value = "/test1", method = RequestMethod.GET)
	public String test1(Locale locale, Model model)
	{
		PowerShell powerShell = null;
		String result = "";

		try {
			// Creates PowerShell session (we can execute several commands in the same session)
			powerShell = PowerShell.openSession();

			// Execute a command in PowerShell session
			PowerShellResponse response = powerShell.executeCommand("Get-Process");

			// Print results
			result += response.getCommandOutput();

			// Execute another command in the same PowerShell session
			response = powerShell.executeCommand("Get-WmiObject Win32_BIOS");

			// Print results
			result += response.getCommandOutput();
		} catch (PowerShellNotAvailableException ex) {
			// Handle error when PowerShell is not available in the system
			// Maybe try in another way?
		} finally {
			// Always close PowerShell session to free resources.
			if (powerShell != null)
				powerShell.close();
		}
		model.addAttribute("commandOutput", result);

		return "home";
	}

	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	public String test2(Locale locale, Model model)
	{
		PowerShell powerShell = null;
		String result = "";

		PowerShellResponse response = null;
		try {
			// Creates PowerShell session
			powerShell = PowerShell.openSession();

			// Increase timeout to give enough time to the script to finish
			Map<String, String> config = new HashMap<String, String>();
			config.put("maxWait", "80000");

			// Execute script
			response = powerShell.configuration(config).executeScript("./myPath/MyScript.ps1");

			// Print results if the script
			result += response.getCommandOutput();
		} catch (PowerShellNotAvailableException ex) {
			// Handle error when PowerShell is not available in the system
			// Maybe try in another way?
		} finally {
			// Always close PowerShell session to free resources.
			if (powerShell != null)
				powerShell.close();
		}

		model.addAttribute("commandOutput", result);

		return "home";
	}
}
