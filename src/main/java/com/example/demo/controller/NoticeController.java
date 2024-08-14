package com.example.demo.controller;

import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
//  根据id修改整个公告(releasetime,effectivetime，textFile,imageFile,annexFile都不能为空)
    @PutMapping ("update")
    public int update(@RequestParam Integer id,
                      @RequestParam(required = false)String theme,
                      @RequestParam(required = false)String category,
                      @RequestParam(required = false)String unit,
                      @RequestParam(required = false)String approver,
                      @RequestParam(required = false)String publisher,
                      @RequestParam(required = false)String releasetime,
                      @RequestParam(required = false)String effectivetime,
                      @RequestParam(required = false)MultipartFile textFile,
                      @RequestParam(required = false)MultipartFile imageFile,
                      @RequestParam(required = false)MultipartFile annexFile,
                      @RequestParam(required = false)String state)
                      throws IOException {

        String textPath = noticeService.storeFile(textFile);
        String imagePath = noticeService.storeFile(imageFile);
        String annexPath = noticeService.storeFile(annexFile);

        Notice data = new Notice();
        data.setId(id);
        data.setTheme(theme);
        data.setCategory(category);
        data.setUnit(unit);
        data.setApprover(approver);
        data.setPublisher(publisher);
        data.setReleasetime(Timestamp.valueOf(releasetime));
        data.setEffectivetime(Timestamp.valueOf(effectivetime));
        data.setText(textPath);
        data.setImage(imagePath);
        data.setAnnex(annexPath);
        data.setState(state);

        return noticeService.updateByPrimaryKeySelective(data);
    }

//  根据id删除整个公告
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String,Object> RS = new HashMap<>();
        if (id == null) {
            RS.put("status", "error");
            RS.put("message", "id为空");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RS);
        }
        boolean isDeleted = noticeService.deleteByPrimaryKey(id);
        if(isDeleted){
            RS.put("status","success");
            RS.put("message","删除了id为："+id+"的公告");
            return ResponseEntity.ok(RS);
        }else{
            RS.put("status","error");
            RS.put("message","删除失败");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RS);
        }
    }

//  待发公告，最新公告，历史公告查询
    @GetMapping("differentnotice")
    public List<Notice> differentnotice(String state){
        return noticeService.selectByState(state);
    }

//  根据id获取单个公告的完整信息
    @GetMapping("selectone")
    public Notice selectone(Integer id) {
        return noticeService.selectByPrimaryKey(id);
    }

//  条件查询
    @GetMapping("condition")
    public List<Notice> condition(@RequestParam(required = false)Integer id,
                                  @RequestParam(required = false)String theme,
                                  @RequestParam(required = false)String category,
                                  @RequestParam(required = false)String unit,
                                  @RequestParam(required = false)String approver,
                                  @RequestParam(required = false)String publisher,
                                  @RequestParam(required = false)Date releasetime,
                                  @RequestParam(required = false)Date effectivetime,
                                  @RequestParam(required = false)String state)
    {
        Notice date = new Notice();
        date.setId(id);
        date.setTheme(theme);
        date.setCategory(category);
        date.setUnit(unit);
        date.setApprover(approver);
        date.setPublisher(publisher);
        date.setReleasetime(releasetime);
        date.setEffectivetime(effectivetime);
        date.setState(state);
        return noticeService.findByCondition(date);
    }

//  关键字查询
    @GetMapping("keyword")
    public List<Notice> keyword(String keyword){
        return noticeService.findByFuzzy(keyword);
    }

//  文件保存和上传
    @PostMapping("/upload")
    public Notice uploadNotice(@RequestParam("id") Integer id,
                               @RequestParam("theme") String theme,
                               @RequestParam("category") String category,
                               @RequestParam("unit") String unit,
//                               @RequestParam(required = false) String approver,
                               @RequestParam("publisher") String publisher,
                               @RequestParam("releasetime") String releasetime,
                               @RequestParam("effectivetime") String effectivetime,
                               @RequestParam("textFile") MultipartFile textFile,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               @RequestParam("annexFile") MultipartFile annexFile)
                               throws IOException {

        String textPath = noticeService.storeFile(textFile);
        String imagePath = noticeService.storeFile(imageFile);
        String annexPath = noticeService.storeFile(annexFile);

        Notice notice = new Notice();
        notice.setId(id);
        notice.setTheme(theme);
        notice.setCategory(category);
        notice.setUnit(unit);
//        notice.setApprover(approver);
        notice.setPublisher(publisher);
        notice.setReleasetime(Timestamp.valueOf(releasetime));
        notice.setEffectivetime(Timestamp.valueOf(effectivetime));
        notice.setText(textPath);
        notice.setImage(imagePath);
        notice.setAnnex(annexPath);
        notice.setState("待发");

        noticeService.saveNotice(notice);
        return notice;
    }

//  文件下载
    @GetMapping("/download/{type}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String type, @PathVariable String filename) throws IOException {
        Path filePath = noticeService.loadFile(filename);
        Resource resource = new UrlResource(filePath.toUri());

        String contentType = "application/octet-stream";
        if ("image".equals(type)) {
            contentType = "image/jpeg"; // or determine content type dynamically
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(resource);
    }

//  打印
    @PostMapping("/print/{id}")
    public String printNotice(@PathVariable Integer id) {
        try {
            Notice notice = noticeService.selectByPrimaryKey(id);
            if (notice == null) {
                return "Notice not found";
            }
            // 生成 PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.add(new Paragraph("Theme: " + notice.getTheme()));
            document.add(new Paragraph("Category: " + notice.getCategory()));
            document.add(new Paragraph("Unit: " + notice.getUnit()));
            document.add(new Paragraph("Approver: " + notice.getApprover()));
            document.add(new Paragraph("Publisher: " + notice.getPublisher()));
            document.add(new Paragraph("Release Time: " + notice.getReleasetime()));
            document.add(new Paragraph("Effective Time: " + notice.getEffectivetime()));
            document.add(new Paragraph("State: " + notice.getState()));
            // 读取文本文件内容并添加到 PDF
            Path textFilePath = noticeService.loadFile(notice.getText());
            System.out.println("Text file path: " + textFilePath.toString());
            String textContent = new String(Files.readAllBytes(textFilePath));
            document.add(new Paragraph("Text: " + textContent));
            // 读取图片文件并添加到 PDF
            Path imageFilePath = noticeService.loadFile(notice.getImage());
            System.out.println("Image file path: " + imageFilePath.toString());
            ImageData imageData = ImageDataFactory.create(Files.readAllBytes(imageFilePath));
            Image image = new Image(imageData);
            image.scaleToFit(500, 500); // 调整图片大小
            document.add(image);
            // 读取附件文件并添加到 PDF
            Path annexFilePath = noticeService.loadFile(notice.getAnnex());
            System.out.println("Annex file path: " + annexFilePath.toString());
            String annexContent = new String(Files.readAllBytes(annexFilePath));
            document.add(new Paragraph("Annex: " + annexContent));

            document.close();

            // 打印 PDF
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                return "No print service found";
            }

            SimpleDoc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(new Copies(1));
            attributes.add(MediaSizeName.ISO_A4);
            attributes.add(Sides.ONE_SIDED);
            printService.createPrintJob().print(doc, attributes);

            return "Print job submitted";
        } catch (IOException | com.itextpdf.kernel.PdfException | PrintException e) {
            e.printStackTrace();
            return "Failed to print notice";
        }
    }

//  审核
    @PostMapping("/approve")
    public Notice approveNotice(@RequestParam("id") Integer id, @RequestParam(value = "approver", required = false) String approver) {
        // 根据id查询Notice对象
        Notice notice = noticeService.selectByPrimaryKey(id);
        if (notice != null) {
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            // 获取Notice对象的发布时间
            LocalDateTime releaseTime = notice.getReleasetime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 如果当前时间等于或晚于发布时间，则将Notice对象的状态设置为"新发"
            if (now.isEqual(releaseTime) || now.isAfter(releaseTime)) {
                notice.setState("新发");
            }
            // 如果Notice对象的approver为空，则将approver设置为传入的approver
            if (notice.getApprover() == null || notice.getApprover().isEmpty()) {
                notice.setApprover(approver);
            }
            // 更新Notice对象
            noticeService.updateByPrimaryKeySelective(notice);
        }
        // 返回Notice对象
        return notice;
    }

//  检查并更新已过有效期的公告状态
    @PostMapping("/updateExpiredNotices")
    public void updateExpiredNotices() {
        noticeService.updateExpiredNotices();
    }

}
