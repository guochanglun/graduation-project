package com.gcl.server.controller;

import com.gcl.server.bean.Image;
import com.gcl.server.bean.User;
import com.gcl.server.exception.StorageFileNotFoundException;
import com.gcl.server.repository.ImgRepository;
import com.gcl.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path="/img")
public class ImageController {

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private ImgRepository imgRepository;

    private final StorageService storageService;

    @Autowired
    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 根据文件名获取图片数据
     */
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * 获取用户上传的图片列表
     */
    @GetMapping
    @ResponseBody
    public List<Image> imgList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return imgRepository.findByUserId(user.getId());
    }

    /**
     * 上传图片
     */
    @PostMapping
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("imgDesc") String imgDesc,
                                                 HttpSession session) {
        String filename = "img"+System.currentTimeMillis();
        storageService.store(file, filename);

        User user = (User) session.getAttribute("user");
        System.out.println(user);
        // 添加路径到数据库
        Image image = new Image();
        image.setImgDesc(imgDesc);
        image.setImgName(filename);
        image.setUserId(user.getId());
        imgRepository.save(image);

        return baseUrl + "/img/" + filename;
    }

    /**
     * 删除图片
     */
    @DeleteMapping("/{filename}")
    public @ResponseBody String delete(@PathVariable("filename") String filename){
        // 删除数据库信息
        imgRepository.deleteByImgName(filename);

        // 删除文件
        if(storageService.delete(filename)){
            return "ok";
        }
        return "error";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
