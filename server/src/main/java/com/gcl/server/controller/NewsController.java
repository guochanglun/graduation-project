package com.gcl.server.controller;

import com.gcl.server.bean.Article;
import com.gcl.server.bean.News;
import com.gcl.server.bean.User;
import com.gcl.server.repository.ArticleRepository;
import com.gcl.server.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${clsUrl}")
    private String clsUrl;

    @Value("${baseUrl}")
    private String baseUrl;

    /**
     * 首页新闻
     */
    @GetMapping("/index")
    public String index(Model model){
        List<News> news =  newsRepository.custom();
        model.addAttribute("news", news);
        model.addAttribute("baseUrl", baseUrl);
        return "news";
    }
    /**
     * 返回用户所有的新闻列表
     */
    @GetMapping
    public @ResponseBody  List<News> all(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return new ArrayList<>();
        }
        return newsRepository.findByUserId(user.getId());
    }

    /**
     * 根据类别返回页面
     */
    @GetMapping("/{tag}/{page}")
    public String getByTag(@PathVariable("tag") String tag,
                           @PathVariable("page") int page,
                           Model model){
        List<News> news = newsRepository.findByTagAndPage(tag, page, page+10);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("news", news);
        return "news";
    }

    /**
     * 删除一条新闻， 顺便带着把article删除了
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public @ResponseBody String delete(@PathVariable("id") Integer id){
        News news = newsRepository.findOne(id);
        // 删除news
        newsRepository.delete(id);
        // 删除article
        articleRepository.delete(news.getArticleId());
        return "ok";
    }

    @GetMapping("/delete/{id}")
    public @ResponseBody String deleteForGet(@PathVariable("id") Integer id){
        News news = newsRepository.findOne(id);
        // 删除news
        newsRepository.delete(id);
        // 删除article
        articleRepository.delete(news.getArticleId());
        return "ok";
    }

    /**
     * 添加一条新闻, 添加article
     */
    @PostMapping
    public @ResponseBody String add(@RequestParam("html") String html,
                                    @RequestParam("title") String title,
                                    @RequestParam("abstracts") String abstracts,
                                    HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "error";
        }

        // 获取tag
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("html", html);
        String tag = restTemplate.postForObject(clsUrl, map, String.class);
        System.out.println(tag);

        // save article
        Article article = new Article();
        article.setContent(html);
        article.setTitle(title);
        article.setTag(tag);
        article.setDetailSource(user.getName());
        article = articleRepository.save(article);

        // save news
        News news = new News();
        news.setTitle(title);
        news.setUserId(user.getId());
        news.setAbstracts(abstracts);
        news.setMediaName(user.getName());
        Document doc = Jsoup.parse(html);
        Elements eles = doc.select("img");
        if(eles.size() > 1){
            String imgList = "";
            for(Element ele : eles){
                imgList += ele.attr("src") + ";";
            }
            news.setImageList(imgList);
        }else if(eles.size() == 1){
            Element ele = eles.first();
            String imageUrl = ele.attr("src");
            news.setImageUrl(imageUrl);
        }
        news.setTag(tag);
        news.setArticleId(article.getId());
        newsRepository.save(news);
        return "ok";
    }

    /**
     * 返回新闻页面
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String findArticle(@PathVariable("id") Integer id, Model model){
        Article article = articleRepository.findOne(id);
        model.addAttribute("article", article);
        return "article";
    }

    /**
     * 返回一篇article Json
     */
    @GetMapping("/article/{id}")
    public @ResponseBody  Article findArticleJson(@PathVariable("id") Integer id){
        return articleRepository.findOne(id);
    }

    /**
     * 返回所有的新闻
     */
    @GetMapping("/all")
    public @ResponseBody Iterable<News> all(){
        return newsRepository.findAll();
    }
}
