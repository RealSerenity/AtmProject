package com.rserenity.atmproject.ui.controller;

import com.rserenity.atmproject.Exception.ProcessInfo.Message;
import com.rserenity.atmproject.Exception.ProcessInfo.MessageType;
import com.rserenity.atmproject.bean.MyPagination;
import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import com.rserenity.atmproject.ui.rest.AccountController;
import com.rserenity.atmproject.ui.rest.BankController;
import com.rserenity.atmproject.ui.rest.CustomerController;
import com.rserenity.atmproject.ui.rest.RoleController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
public class ThymeleafController {

    @Autowired
    CustomerController customerController;

    @Autowired
    BankController bankController;

    @Autowired
    AccountController accountController;

    @Autowired
    MyPagination<AccountDto> accountDtoPagination;

    @Autowired
    RoleController roleController;

    @Autowired
    Message pageMessage;

    @GetMapping("/api/userRegister")
    public String userRegister(Model model){
        model.addAttribute("customerDto", CustomerDto.builder().build());
        return "thymeleafRegister";
    }

    @PostMapping("/api/userRegister")
    public String postRegisterForm(Model model, @Valid @ModelAttribute("customerDto") CustomerDto customerDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("Binding hatası var !!!");
            return "thymeleafRegister";
        }
        log.info("Binding hatası yok !");
        CustomerEntity customerFromDb = customerController.getCustomerByName(customerDto.getUsername());
        if(customerFromDb == null){
            customerController.createCustomer(customerDto);
            log.error("Yeni üye oluşturuldu username : " + customerDto.getUsername());
            // yeni üye oluştur
            // login sayfasına yönelt
            return "thymeleafLogin";
        }else {
            model.addAttribute("userexists","Kullanıcı adı kullanılıyor!");
            // username must be unique
            // tekrar register sayfasına yönelt
            return "thymeleafRegister";
        }

        // database kaydetme

    }

    @GetMapping("/api/userLogin")
    public String userLogin(Model model){
        model.addAttribute("customerDto", CustomerDto.builder().build());
        return "thymeleafLogin";
    }

    @PostMapping("/api/userLogin")
    public String postLoginForm(Model model ,@Valid @ModelAttribute("customerDto") CustomerDto customerDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("Hata var !!!");
            return "thymeleafLogin";
        }
        if(customerController.getCustomerByName(customerDto.getUsername()) == null){
            model.addAttribute("unknownUsername","Kullanıcı bulunamadı!");
            return "thymeleafLogin";
        }else{
            if(customerController.customerLogin(customerDto)){
                log.info("Kullanıcı giriş yaptı username : " + customerDto.getUsername());
                CustomerEntity  customerEntity = customerController.getCustomerByName(customerDto.getUsername());
                model.addAttribute("customerId",customerEntity.getId());
                return "redirect:/api/homePage?customerId="+customerEntity.getId();
            }else {
                log.error("şifre yanlış");
                model.addAttribute("wrongPassword","Şifre yanlış!");
                return "thymeleafLogin";
            }
        }

        // database kaydetme
        // dosyaya yazma
    }

    @GetMapping("/api/homePage")
    public String homePage(Model model, @RequestParam(name = "customerId") Long customerId) throws Throwable {
        model.addAttribute("customerId",customerId);
        // check if the customer has admin role
        model.addAttribute("isCustomerAdmin",customerController.getCustomerById(customerId).getBody().getRoles().contains(roleController.getRoleByName("admin")));
        return "homePage";
    }



    @GetMapping(value = "/api/customerPage")
    public String userPage(Model model, @RequestParam(name = "customerId") Long customerId, @RequestParam(name = "page" ,defaultValue ="1") int page) throws Throwable {
        boolean isAdmin = customerController.getCustomerById(customerId).getBody().getRoles().contains(roleController.getRoleByName("admin"));
        model.addAttribute("isCustomerAdmin", isAdmin);
        if(!isAdmin){
            List<AccountDto> bankAccountsList = accountController.getCustomerAccountsByCustomerId(customerId);
            List<AccountDto> bankAccounts = accountDtoPagination.getPageElementsIns(4,bankAccountsList,page);
            if(accountDtoPagination.getPageCount(4,bankAccountsList) == 0){
                model.addAttribute("pageCount", 1);
            }else{
                model.addAttribute("pageCount", accountDtoPagination.getPageCount(4,bankAccountsList));
            }
            model.addAttribute("customerId",customerId);
            model.addAttribute("bankAccounts", bankAccounts);
            model.addAttribute("createAccount",AccountDto.builder().build());
        }else{
            //admin
            List<AccountDto> allAccounts = accountController.getAllAccounts();
            List<AccountDto> pageAccounts = accountDtoPagination.getPageElementsIns(8,allAccounts,page);
            if(accountDtoPagination.getPageCount(8,allAccounts) == 0){
                model.addAttribute("pageCount", 1);
            }else{
                model.addAttribute("pageCount", accountDtoPagination.getPageCount(8,allAccounts));
            }
            model.addAttribute("customerController", customerController);
            model.addAttribute("allAccounts", pageAccounts);
            model.addAttribute("customerId",customerId);
        }
        model.addAttribute("bankController",bankController);
        model.addAttribute("banks",bankController.getAllBanks());
        model.addAttribute("pageIndex",page);
        return "customerPage";
    }

    @PostMapping("/api/createAccount")
    private String createAccount(Model model,@ModelAttribute("createAccount") AccountDto accountDto,  @RequestParam(name = "customerId") Long customerId, RedirectAttributes redirectAttributes){
        accountDto.setCustomerId(customerId);
        accountDto.setMoney(0L);
        AccountDto createdAccount = accountController.createAccount(accountDto);
        redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Success,"Yeni hesap oluşturuldu"));
        log.info("new account created -> " + createdAccount);
        return "redirect:/api/customerPage?customerId="+customerId;
    }

    @GetMapping("/api/accountPage")
    public String accountPage(Model model, @RequestParam(name = "customerId") Long customerId, @RequestParam(name = "accountId") Long accountId) throws Throwable {
        model.addAttribute("bankname", bankController.getBankById(accountController.getAccountById(accountId).getBody().getBankId()).getBody().getBankName());
        model.addAttribute("currentMoney", accountController.getAccountById(accountId).getBody().getMoney());
        model.addAttribute("customerId", customerId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("accountDtoYatir", AccountDto.builder().build());
        model.addAttribute("accountDtoCek", AccountDto.builder().build());
        model.addAttribute("accountDtoAktar", AccountDto.builder().build());
        return "accountPage";
    }

    @PostMapping("/api/paraYatır")
    public String userParaYatır(Model model, @Valid @ModelAttribute("accountDtoYatir") AccountDto accountDto,  @RequestParam(name = "customerId") Long customerId,
                                @RequestParam(name = "accountId") Long accountId, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Throwable {
        if (bindingResult.hasErrors()){
            log.error("Hata var !!!");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Bilinmeyen hata"));
            return "redirect:/api/customerPage?customerId="+customerId;
        }
        if(accountDto.getMoney() == null){
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Lütfen form bilgisini(Para Miktarı) doldurunuz !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        ResponseEntity<AccountDto> dbaccountDto =  accountController.getAccountById(accountId);
        if(dbaccountDto.getBody() == null){
            log.error("Hesap bulunamadı !!!");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Hesap bulunamadı"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        dbaccountDto.getBody().setMoney(dbaccountDto.getBody().getMoney() + accountDto.getMoney());
        accountController.updateAccount(accountId,
                AccountDto
                        .builder()
                        .customerId(dbaccountDto.getBody().getCustomerId())
                        .money(dbaccountDto.getBody().getMoney())
                        .bankId(dbaccountDto.getBody().getBankId())
                        .id(dbaccountDto.getBody().getId())
                        .build());

            log.info("Para yatırıldı account id : " + accountId + " yatırılan tutar : " + accountDto.getMoney());
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Success,"Para yatırıldı"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }

    @PostMapping("/api/paraCek")
    public String userParaCek(Model model, @Valid @ModelAttribute("accountDtoCek") AccountDto accountDto,  @RequestParam(name = "customerId") Long customerId,
                              @RequestParam(name = "accountId") Long accountId, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Throwable {
        ResponseEntity<AccountDto> dbaccountDto =  accountController.getAccountById(accountId);
        if (bindingResult.hasErrors()){
            log.error("Hata var !!!");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Bilinmeyen hata"));
            return "redirect:/api/customerPage?customerId="+customerId;
        }
        if(accountDto.getMoney() == null){
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Lütfen form bilgisini(Para Miktarı) doldurunuz !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        if(dbaccountDto.getBody() == null){
            log.error("Hesap bulunamadı !!!");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Hesap bulunamadı !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
            if(dbaccountDto.getBody().getMoney() >= accountDto.getMoney()){
                //yeterli para var
                log.info("Yeterli para var, para çekiliyor ...");
                dbaccountDto.getBody().setMoney(dbaccountDto.getBody().getMoney() - accountDto.getMoney());
                accountController.updateAccount(accountId,
                        AccountDto
                                .builder()
                                .customerId(dbaccountDto.getBody().getCustomerId())
                                .money(dbaccountDto.getBody().getMoney())
                                .bankId(dbaccountDto.getBody().getBankId())
                                .id(dbaccountDto.getBody().getId())
                                .build());

                log.info("Para çekildi account id : " + accountId + " çekilen tutar : " + accountDto.getMoney());
                redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Success,"Para çekildi"));
                return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }else {
                //yeterli para yok
                log.error("Yeterli para yok işlem iptal ediliyor ...");
                redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Hesapta yeterli para yok"));
                return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
    }

    @PostMapping("/api/paraAktar")
    public String userParaAktar(Model model, @Valid @ModelAttribute("accountDtoAktar") AccountDto accountDto,  @RequestParam(name = "customerId") Long customerId,
                                @RequestParam(name = "accountId") Long accountId, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Throwable {
        ResponseEntity<AccountDto> dbaccountDto =  accountController.getAccountById(accountId);
        Message message = new Message();
        if (bindingResult.hasErrors()){
            log.error("Hata var !!!");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Bilinmeyen hata"));
            return "redirect:/api/customerPage?customerId="+customerId;
        }
        if(accountDto.getMoney() == null){
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Lütfen form bilgisini(Para Miktarı) doldurunuz !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        if(accountDto.getId() == null){
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Error,"Lütfen form bilgisini(Aktarılacak Hesap Idsi) doldurunuz !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        if(dbaccountDto.getBody() == null){
            log.error("Hesap bulunamadı !!!");
//            message.setMessageType(Message.MessageType.Unknown, "");
                redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Hesap bulunamadı !"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }
        if(dbaccountDto.getBody().getMoney() >= accountDto.getMoney()){
            //yeterli para var
            log.info("Yeterli para var, para aktarılıyor ...");
            ResponseEntity<AccountDto> dbAktarilacakHesap =  accountController.getAccountById(accountDto.getId());
            if(dbAktarilacakHesap.getBody() == null){
                log.error("Aktarılacak hesap bulunamadı !!!");
                redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Para aktarılacak hesap bulunamadı !"));
                return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
            }
            dbaccountDto.getBody().setMoney(dbaccountDto.getBody().getMoney() - accountDto.getMoney());
            accountController.updateAccount(accountId,
                    AccountDto
                            .builder()
                            .customerId(dbaccountDto.getBody().getCustomerId())
                            .money(dbaccountDto.getBody().getMoney())
                            .bankId(dbaccountDto.getBody().getBankId())
                            .id(dbaccountDto.getBody().getId())
                            .build());

            dbAktarilacakHesap.getBody().setMoney(dbAktarilacakHesap.getBody().getMoney() + accountDto.getMoney());
            accountController.updateAccount(accountDto.getId(),
                    AccountDto
                            .builder()
                            .customerId(dbAktarilacakHesap.getBody().getCustomerId())
                            .money(dbAktarilacakHesap.getBody().getMoney())
                            .bankId(dbAktarilacakHesap.getBody().getBankId())
                            .id(dbAktarilacakHesap.getBody().getId())
                            .build());

            log.info("Para aktarıldı account id : " + accountId + " hedef account id : " + accountDto.getId() + " aktarılan tutar : " + accountDto.getMoney());
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Success,"Para aktarıldı"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;
        }else {
            //yeterli para yok
            log.error("Yeterli para yok işlem iptal ediliyor ...");
            redirectAttributes.addFlashAttribute("infoMessage", pageMessage.getMessageTypeAndMessage(MessageType.Failed,"Hesapta yeterli para yok"));
            return "redirect:/api/accountPage?customerId="+customerId+"&accountId="+accountId;

        }
    }

}
