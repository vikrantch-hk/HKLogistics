package com.hk.logistics.config;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.hk.logistics.domain.Awb;
import com.hk.logistics.domain.AwbStatus;
import com.hk.logistics.domain.VendorWHCourierMapping;
import com.hk.logistics.repository.VendorWHCourierMappingRepository;
import com.hk.logistics.service.dto.AwbDTO;
import com.hk.logistics.service.dto.AwbStatusDTO;

@Component("awbExcelView")
public class AwbExcelView extends AbstractXlsxView {
	
	@Autowired
	private VendorWHCourierMappingRepository vendorWHCourierMappingRepository;

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		      List<AwbDTO> awbList = (List<AwbDTO>) model.get("Awb");
		      Sheet sheet = workbook.createSheet("Awb");
		      sheet.setFitToPage(true);

		      int rowCount = 0;
		      Row header = sheet.createRow(rowCount++);
		      header.createCell(0).setCellValue("COURIER_CHANNEL_ID");
		      header.createCell(1).setCellValue("AWB_NUMBER");
		      header.createCell(2).setCellValue("COD");

		      for (AwbDTO awbDto : awbList) {
		          Row currencyRow = sheet.createRow(rowCount++);
		          VendorWHCourierMapping vendorWHCourierMapping = vendorWHCourierMappingRepository.findById(awbDto.getVendorWHCourierMappingId()).get();
		          //vendorWHCourierMapping.getCourierChannel().getCouriers();
		          currencyRow.createCell(0).setCellValue(vendorWHCourierMapping.getCourierChannel().getChannel().getName());
		          currencyRow.createCell(1).setCellValue(awbDto.getAwbNumber());
		          currencyRow.createCell(2).setCellValue(awbDto.isCod());
		      }
		      response.setHeader("Content-Disposition", "attachment; filename=awbs.xls");}

}
