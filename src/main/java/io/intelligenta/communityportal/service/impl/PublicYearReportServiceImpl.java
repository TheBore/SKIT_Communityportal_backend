package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.models.report.PublicYearReport;
import io.intelligenta.communityportal.models.report.Report;
import io.intelligenta.communityportal.models.report.ReportStatus;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.PublicYearReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@Service
public class PublicYearReportServiceImpl implements PublicYearReportService {

    private ReportRepository reportRepository;
    private PublicYearReportRepository publicYearReportRepository;
    private AttachmentRepository attachmentRepository;
    private InstitutionRepository institutionRepository;
    private Environment environment;
    private UserRepository userRepository;

    public PublicYearReportServiceImpl(ReportRepository reportRepository, PublicYearReportRepository publicYearReportRepository, AttachmentRepository attachmentRepository, InstitutionRepository institutionRepository, Environment environment, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.publicYearReportRepository = publicYearReportRepository;
        this.attachmentRepository = attachmentRepository;
        this.institutionRepository = institutionRepository;
        this.environment = environment;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PublicYearReport saveReport(String podatociSluzbLice, Integer brPrimeniBaranja, Integer brPozitivnoOdgBaranja,
                                       String odbieniIOtfrleniZalbi, Integer brNeodogovoreniBaranja,
                                       String vlozeniZalbi, Integer brUsvoeniZalbi, Integer brPreinaceniOdluki,
                                       String odbieniZalbi, String otfrelniZalbi, Long institutionId, Integer year) {
        List<PublicYearReport> list = publicYearReportRepository.findByInstitutionIdAndYear(institutionId, year);
        PublicYearReport doc = list.size() == 0 ? null : list.get(0);
        if (doc == null) {
            Institution institution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);
            Report report = new Report();
            report.setPodatociSluzbLice(podatociSluzbLice);
            report.setBrPrimeniBaranja(brPrimeniBaranja);
            report.setBrPozitivnoOdgBaranja(brPozitivnoOdgBaranja);
            report.setOdbieniIOtfrleniZalbi(odbieniIOtfrleniZalbi);
            report.setBrNeodogovoreniBaranja(brNeodogovoreniBaranja);
            report.setVlozeniZalbi(vlozeniZalbi);
            report.setBrUsvoeniZalbi(brUsvoeniZalbi);
            report.setBrPreinaceniOdluki(brPreinaceniOdluki);
            report.setOdbieniZalbi(odbieniZalbi);
            report.setOtfrelniZalbi(otfrelniZalbi);
            reportRepository.save(report);

            PublicYearReport pyr = new PublicYearReport();
            pyr.setYear(year);
            pyr.setStatus(ReportStatus.SAVED);
            pyr.setInstitution(institution);
            pyr.setReport(report);
            publicYearReportRepository.save(pyr);

            return pyr;
        } else throw new PublicYearReportAlreadyExistException();
    }

    @Override
    @Transactional
    public void submitReport(Long pyrId) throws Exception {
        PublicYearReport pyr = publicYearReportRepository.findById(pyrId).orElse(null);
        String outputFile = environment.getProperty("report.path") + "/izvestaj" + pyr.getId() + ".pdf"; // patekata za zacuvuvanje na report
        List<User> users = pyr.getInstitution().getUsers().stream()
                .filter(user -> user.getRole().equals(UserRole.ROLE_INSTITUTIONAL_MODERATOR))
                .collect(Collectors.toList()); // sluzbenoto lice

        String sluzbLice = users.get(0).getFirstName() + " " + users.get(0).getLastName();
        String imatel = pyr.getInstitution().getNameMk() + ", " + pyr.getInstitution().getTypeOfStreetMk() + "." + pyr.getInstitution().getStreetMk() + "бр. " + pyr.getInstitution().getStreetNumberMk() + ", " + pyr.getInstitution().getCityMk() + ", " + pyr.getInstitution().getInstitutionPhone();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date dateobj = new Date();
        String date = df.format(dateobj);


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("imatel", imatel);
        map.put("sluzbLice", sluzbLice);
        map.put("year", String.valueOf(pyr.getYear()));
        map.put("podatociSluzbLice", pyr.getReport().getPodatociSluzbLice());
        map.put("brPrimeniBaranja", pyr.getReport().getBrPrimeniBaranja());
        map.put("brPozitivnoOdgBaranja", pyr.getReport().getBrPozitivnoOdgBaranja());
        map.put("odbieniIOtfrleniZalbi", pyr.getReport().getOdbieniIOtfrleniZalbi());
        map.put("brNeodgovoreniBaranja", pyr.getReport().getBrNeodogovoreniBaranja());
        map.put("vlozeniZalbi", pyr.getReport().getVlozeniZalbi());
        map.put("brUsvoeniZalbi", pyr.getReport().getBrUsvoeniZalbi());
        map.put("brPreinaceniOdluki", pyr.getReport().getBrPreinaceniOdluki());
        map.put("odbieniZalbi", pyr.getReport().getOdbieniZalbi());
        map.put("otfrleniZalbi", pyr.getReport().getOtfrelniZalbi());
        map.put("date", date);
        String templateName = "izvestaj.ftl";

        File attachment = new File(outputFile);
        byte[] content = readFileToByteArray(attachment);
        String name = "report" + pyr.getId()+".pdf";
        String type = "application/pdf";
        Attachment attach = new Attachment();
        attach.setContent(content);
        attach.setMimeType(type);
        attach.setName(name);
        attachmentRepository.save(attach);
        pyr.setDoc(attach);
        pyr.setStatus(ReportStatus.SUBMITTED);
        publicYearReportRepository.save(pyr);
    }

    @Override
    public PublicYearReport updateReport(String podatociSluzbLice, Integer brPrimeniBaranja, Integer brPozitivnoOdgBaranja, String odbieniIOtfrleniZalbi, Integer brNeodogovoreniBaranja, String vlozeniZalbi, Integer brUsvoeniZalbi, Integer brPreinaceniOdluki, String odbieniZalbi, String otfrelniZalbi, Long pyrId) {

        PublicYearReport doc = publicYearReportRepository.findById(pyrId).orElse(null);
        if (doc != null) {
            Report oldReport = reportRepository.findById(doc.getReport().getId()).orElse(null);
            Report report = new Report();
            report.setPodatociSluzbLice(podatociSluzbLice);
            report.setBrPrimeniBaranja(brPrimeniBaranja);
            report.setBrPozitivnoOdgBaranja(brPozitivnoOdgBaranja);
            report.setOdbieniIOtfrleniZalbi(odbieniIOtfrleniZalbi);
            report.setBrNeodogovoreniBaranja(brNeodogovoreniBaranja);
            report.setVlozeniZalbi(vlozeniZalbi);
            report.setBrUsvoeniZalbi(brUsvoeniZalbi);
            report.setBrPreinaceniOdluki(brPreinaceniOdluki);
            report.setOdbieniZalbi(odbieniZalbi);
            report.setOtfrelniZalbi(otfrelniZalbi);
            reportRepository.save(report);

            doc.setReport(report);
            reportRepository.delete(oldReport);
            publicYearReportRepository.save(doc);
            return doc;
        } else throw new PublicDocumentTypeNotFoundException();

    }

    @Override
    @Transactional
    public List<PublicYearReport> findAllByYear(Integer year) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        List<PublicYearReport> reports = publicYearReportRepository.findAllByYear(year);
        if(user.getUserRole().equals(UserRole.ROLE_ADMIN)){
            return reports.stream().filter(report -> report.getStatus().equals(ReportStatus.SUBMITTED)).collect(Collectors.toList());
        }
        else return reports;
    }

    @Override
    public PublicYearReport changeDocument(Long pyrId, byte[] content, String name, String fileContentType, Long size) {
        PublicYearReport pyr = publicYearReportRepository.findById(pyrId).orElse(null);
        if (pyr != null) {
            Long docId = pyr.getDoc().getId();
            Attachment attachment = new Attachment();
            attachment.setContent(content);
            attachment.setMimeType(fileContentType);
            attachment.setName(name);
            attachment.setSize(size);
            attachmentRepository.save(attachment);
            pyr.setDoc(attachment);
            publicYearReportRepository.save(pyr);
            attachmentRepository.deleteById(docId);
        }
        return pyr;
    }

    @Override
    @Transactional
    public byte[] exportToXlsx() {
        List<PublicYearReport> list = publicYearReportRepository.findAll();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            File file = new File("Export.xlsx");
            XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
            int rownum = 0;

            CellStyle cs = workbook.createCellStyle();
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            cs.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cs.setFont(font);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
            Row row = sheet.createRow(rownum++);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));
            Cell cell = row.createCell(0);
            cell.setCellValue("Ред бр.");
            cell.setCellStyle(cs);

            sheet.addMergedRegion(CellRangeAddress.valueOf("B1:B2"));
            cell = row.createCell(1);
            cell.setCellValue("Институција");
            cell.setCellStyle(cs);

            sheet.addMergedRegion(CellRangeAddress.valueOf("C1:C2"));
            cell = row.createCell(2);
            cell.setCellValue("Службено лице");
            cell.setCellStyle(cs);

            sheet.addMergedRegion(CellRangeAddress.valueOf("D1:D2"));
            cell = row.createCell(3);
            cell.setCellValue("Година на извештај");
            cell.setCellStyle(cs);

            sheet.addMergedRegion(CellRangeAddress.valueOf("E1:E2"));
            cell = row.createCell(4);
            cell.setCellValue("Статус на извештај");
            cell.setCellStyle(cs);

            rownum = 2;
            int no = 1;
            for (PublicYearReport report : list) {
                row = sheet.createRow(rownum++);
                createList(report, row, workbook, no);
                no++;

            }
            for (int i = 0; i < 19; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream out = new FileOutputStream(file); // file name with path
            workbook.write(out);
            out.close();
            byte[] bytes = readFileToByteArray(file);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    @Override
    public Attachment addAttachment(Long publicYearReportId, String name, String mimeType, Long size, byte[] content) {

        PublicYearReport publicYearReport = publicYearReportRepository.findById(publicYearReportId).orElseThrow(AnnouncementPublicationNotFoundException::new);

        Attachment attachment = publicYearReport.getSignedDoc();

        if(attachment != null) {
            attachment.setName(name);
            attachment.setMimeType(mimeType);
            attachment.setSize(size);
            attachment.setContent(content);
            publicYearReport.setSignedDoc(attachment);

            return attachmentRepository.save(attachment);
        }
        else {
            Attachment newAttachment = new Attachment();
            newAttachment.setName(name);
            newAttachment.setMimeType(mimeType);
            newAttachment.setSize(size);
            newAttachment.setContent(content);
            publicYearReport.setSignedDoc(newAttachment);

            return attachmentRepository.save(newAttachment);
        }


    }

    @Override
    public PublicYearReport findById(Long Id) {

        PublicYearReport publicYearReport = publicYearReportRepository.findById(Id).orElseThrow(PublicYearReportNotFoundException::new);

        return publicYearReport;
    }


    @Transactional
    void createList(PublicYearReport report, Row row, XSSFWorkbook workbook, Integer no) // creating cells for each row
    {
        List<User> users = report.getInstitution().getUsers().stream()
                .filter(user -> user.getRole().equals(UserRole.ROLE_INSTITUTIONAL_MODERATOR))
                .collect(Collectors.toList()); // sluzbenoto lice
        String sluzbLice = users.get(0).getFirstName() + " " + users.get(0).getLastName();

        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);   //Wrapping text
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        Cell cell = row.createCell(0);
        cell.setCellValue(no);
        cell.setCellStyle(cs);

        cell = row.createCell(1);
        cell.setCellValue(report.getInstitution().getNameMk());
        cell.setCellStyle(cs);

        cell = row.createCell(2);
        cell.setCellValue(sluzbLice);
        cell.setCellStyle(cs);

        cell = row.createCell(3);
        cell.setCellValue(report.getYear());
        cell.setCellStyle(cs);

        cell = row.createCell(4);
        if (report.getStatus().equals(ReportStatus.SUBMITTED)) {
            cell.setCellValue("Поднесен");
        } else {
            cell.setCellValue("Зачуван");
        }
        cell.setCellStyle(cs);

    }

    @Override
    @Transactional
    public List<PublicYearReport> findByInstitutionAndYear(Integer year, Long institutionId) {
        return publicYearReportRepository.findByInstitutionIdAndYear(institutionId, year);
    }

    private static byte[] readFileToByteArray(File file) {
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bArray;
    }
}
