package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //15~18라인 기능 대체
public class BasicItemController {
    private final ItemRepository itemRepository;

//    @Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model
    ){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item")/*64라인 수행*/ Item item){
        itemRepository.save(item);
//        model.addAttribute("item",item); Model Attribute 가 view에 자동으로 넣어줌
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){//("이름") 생략 시 클래스명의 첫글자를 소문자로 만들고 넣어줌
        itemRepository.save(item);
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItemV4(Item item){//임의의 객체에는 ModelAttribute가 자동으로 적용되어 생략가
        itemRepository.save(item);
        return "basic/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return"redirect:/basic/items/{itemId}";
    }


    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 10000,20));
    }
}
