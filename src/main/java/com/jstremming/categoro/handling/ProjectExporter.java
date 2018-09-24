package com.jstremming.categoro.handling;

import com.jstremming.categoro.util.Console;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectExporter {

	private final File projectPath;
	private final ProjectConfig projectConfig;

	public ProjectExporter(final File projectPath, final ProjectConfig projectConfig) {
		this.projectPath = projectPath;
		this.projectConfig = projectConfig;
	}

	/**
	 * Creates an .XLS document with project data
	 * stored in project directory
	 * @return if the operation succeeds
	 */
	public boolean exportToExcel() {
		// create the output file without overwriting old ones
		File file = new File(projectPath, projectConfig.getProjectName() + "-out.xls");
		for (int num = 0; file.exists(); num++) {
			file = new File(projectPath, projectConfig.getProjectName() + "-out-" + num + ".xls");
		}

		try (final HSSFWorkbook workbook = exportToWorkbook()) {
			// write to the file
			workbook.write(file);
		} catch (final Exception e) {
			e.printStackTrace();
			Console.severe("An exception occured while exporting to Excel!");
			return false;
		}

		Console.info(".xls file generated: ", file.getAbsolutePath());
		return true;
	}

	/**
	 * Exports project data to an Excel workbook
	 * each category gets a sheet
	 */
	private HSSFWorkbook exportToWorkbook() {
		final Collection<String> categories = projectConfig.getCategories().values();
		final HSSFWorkbook workbook = new HSSFWorkbook();

		// reusable variables for low-memory
		HSSFSheet sheet;
		HSSFRow rowhead;
		HSSFRow row;
		List<String> imgs;

		// output each category into a sheet
		for (final String category : categories) {
			// create a sheet for the category
			sheet = workbook.createSheet(category);

			// output the header
			rowhead = sheet.createRow(0);
			rowhead.createCell(0).setCellValue("File Name");

			imgs = getFilesForCategory(category);
			for (final String img : imgs) {
				// create the row
				row = sheet.createRow(sheet.getLastRowNum() + 1);

				// output data
				row.createCell(0).setCellValue(img);
			}
		}

		return workbook;
	}

	/**
	 * Returns a list of all files in a folder
	 */
	private List<String> getFilesForCategory(final String category) {
		final File[] files = new File(projectPath, category).listFiles();
		final List<String> result = new ArrayList<>();

		// stop if empty
		if (files == null) {
			return result;
		}

		String fileName;
		for (final File file : files) {

			// skip non-files
			if (!file.isFile()) {
				continue;
			}

			// add to list if image
			fileName = file.getName();
			if (fileName.toLowerCase().matches(".*(png|jpg|jpeg).*")) {
				result.add(fileName);
			}
		}

		return result;
	}

}
