package controller;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import dao.*;
import utils.SqlServerConnect;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import view.*;
import javafx.stage.Stage;
import model.accounts.*;
import model.gym.Branch;
import model.gym.Equipment;
import model.gym.members.*;


public class MainController {
    protected boolean isGuest;
    protected boolean isAdmin;
    private User currentUser;
    private Member currentMember;
    private Trainer currentTrainer;
    private Branch currentBranch;
    private Equipment currentEquipment;
    private Session currentSession;
    private MembershipType currentMembershipType;
    private Payment currentPayment;
    private MembershipType selectedMembership;
    private boolean isExtending;
    private boolean isFreezing;

    private MemberDAO memberDAO;
    private UserDAO userDAO;
    private MembershipTypeDAO membershipTypeDao;
    private BranchDAO branchDAO;
    private TrainerDAO trainerDAO;
    private SessionDAO sessionDAO;
    private BookingDAO bookingDAO;
    private EquipmentDAO equipmentDAO;
    private PaymentDAO paymentDAO;

    public MainController(Stage stage){
        try{
            Connection conn = SqlServerConnect.getConnection();
            memberDAO = new MemberDAO(conn);
            userDAO = new UserDAO(conn);
            membershipTypeDao = new MembershipTypeDAO(conn);
            branchDAO = new BranchDAO(conn);
            trainerDAO = new TrainerDAO(conn);
            sessionDAO = new SessionDAO(conn);
            bookingDAO = new BookingDAO(conn);
            equipmentDAO = new EquipmentDAO(conn);
            paymentDAO = new PaymentDAO(conn);
            initializeScenes();
            this.stage=stage;
            stage.setScene(loginScene);
            loginController.clearFields();
            stage.setResizable(true);
            stage.setTitle("Air Gym");
            Image logo = new Image(getClass().getResourceAsStream("../view/assets/images/LOGO.png"));
            stage.getIcons().add(logo);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void initializeScenes() throws Exception{
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("../view/Dashboard.fxml"));
        dashboardScene = new Scene(dashboardLoader.load());
        dashboardController =(DashboardController) dashboardLoader.getController();
        dashboardController.setMain(this);

        FXMLLoader memberViewLoader = new FXMLLoader(getClass().getResource("../view/MemberView.fxml"));
        memberViewScene = new Scene(memberViewLoader.load());
        memberViewController = (MemberViewController) memberViewLoader.getController();
        memberViewController.setMain(this);

        FXMLLoader trainerViewLoader = new FXMLLoader(getClass().getResource("../view/TrainerView.fxml"));
        trainerViewScene = new Scene(trainerViewLoader.load());
        trainerViewController = (TrainerViewController) trainerViewLoader.getController();
        trainerViewController.setMain(this);

        FXMLLoader branchViewLoader = new FXMLLoader(getClass().getResource("../view/BranchView.fxml"));
        branchViewScene = new Scene(branchViewLoader.load());
        branchViewController = (BranchViewController) branchViewLoader.getController();
        branchViewController.setMain(this);

        FXMLLoader equipmentViewLoader = new FXMLLoader(getClass().getResource("../view/EquipmentView.fxml"));
        equipmentViewScene = new Scene(equipmentViewLoader.load());
        equipmentViewController = (EquipmentViewController) equipmentViewLoader.getController();
        equipmentViewController.setMain(this);

        FXMLLoader sessionViewLoader = new FXMLLoader(getClass().getResource("../view/SessionView.fxml"));
        sessionViewScene = new Scene(sessionViewLoader.load());
        sessionViewController = (SessionViewController) sessionViewLoader.getController();
        sessionViewController.setMain(this);

        FXMLLoader membershipTypeViewLoader = new FXMLLoader(getClass().getResource("../view/MembershipTypeView.fxml"));
        membershipTypeViewScene = new Scene(membershipTypeViewLoader.load());
        membershipTypeViewController = (MembershipTypeViewController) membershipTypeViewLoader.getController();
        membershipTypeViewController.setMain(this);

        FXMLLoader paymentViewLoader = new FXMLLoader(getClass().getResource("../view/PaymentView.fxml"));
        paymentViewScene = new Scene(paymentViewLoader.load());
        paymentViewController = (PaymentViewController) paymentViewLoader.getController();
        paymentViewController.setMain(this);
        
        FXMLLoader memberEditLoader = new FXMLLoader(getClass().getResource("../view/MemberEntry.fxml"));
        memberEditScene = new Scene(memberEditLoader.load());
        memberEditController = (MemberEditController) memberEditLoader.getController();
        memberEditController.setMain(this);

        FXMLLoader trainerEntryLoader = new FXMLLoader(getClass().getResource("../view/TrainerEntry.fxml"));
        trainerEntryScene = new Scene(trainerEntryLoader.load());
        trainerEntryController = (TrainerEntryController) trainerEntryLoader.getController();
        trainerEntryController.setMain(this);

        FXMLLoader branchEntryLoader = new FXMLLoader(getClass().getResource("../view/BranchEntry.fxml"));
        branchEntryScene = new Scene(branchEntryLoader.load());
        branchEntryController = (BranchEntryController) branchEntryLoader.getController();
        branchEntryController.setMain(this);

        FXMLLoader equipmentEntryLoader = new FXMLLoader(getClass().getResource("../view/EquipmentEntry.fxml"));
        equipmentEntryScene = new Scene(equipmentEntryLoader.load());
        equipmentEntryController = (EquipmentEntryController) equipmentEntryLoader.getController();
        equipmentEntryController.setMain(this);

        FXMLLoader sessionEntryLoader = new FXMLLoader(getClass().getResource("../view/SessionEntry.fxml"));
        sessionEntryScene = new Scene(sessionEntryLoader.load());
        sessionEntryController = (SessionEntryController) sessionEntryLoader.getController();
        sessionEntryController.setMain(this);

        // FXMLLoader membershipTypeEntryLoader = new FXMLLoader(getClass().getResource("../view/MembershipTypeEntry.fxml"));
        // membershipTypeEntryScene = new Scene(membershipTypeEntryLoader.load());
        // membershipTypeEntryController = (MembershipTypeEntryController) membershipTypeEntryLoader.getController();
        // membershipTypeEntryController.setMain(this);

        FXMLLoader paymentEntryLoader = new FXMLLoader(getClass().getResource("../view/PaymentEntry.fxml"));
        paymentEntryScene = new Scene(paymentEntryLoader.load());
        paymentEntryController = (PaymentEntryController) paymentEntryLoader.getController();
        paymentEntryController.setMain(this);


        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
        loginScene = new Scene(loginLoader.load());
        loginController =(LoginController) loginLoader.getController();
        loginController.setMain(this);

        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("../view/MemberHome.fxml"));
        homeScene = new Scene(homeLoader.load());
        homeController =(HomeController) homeLoader.getController();
        homeController.setMain(this);

        FXMLLoader bookingLoader = new FXMLLoader(getClass().getResource("../view/BookSession.fxml"));
        bookingScene = new Scene(bookingLoader.load());
        bookingController =(BookingController) bookingLoader.getController();
        bookingController.setMain(this);

        FXMLLoader membershipsLoader = new FXMLLoader(getClass().getResource("../view/Memberships.fxml"));
        membershipsScene = new Scene(membershipsLoader.load());
        membershipsController =(MembershipsController) membershipsLoader.getController();
        membershipsController.setMain(this);

        FXMLLoader checkoutLoader = new FXMLLoader(getClass().getResource("../view/Checkout.fxml"));
        checkoutScene = new Scene(checkoutLoader.load());
        checkoutController =(CheckoutController) checkoutLoader.getController();
        checkoutController.setMain(this);

        FXMLLoader profileLoader = new FXMLLoader(getClass().getResource("../view/ProfilePage.fxml"));
        profileScene = new Scene(profileLoader.load());
        profileController =(ProfileController) profileLoader.getController();
        profileController.setMain(this);

        FXMLLoader contactUsLoader = new FXMLLoader(getClass().getResource("../view/ContactUs.fxml"));
        contactUsScene = new Scene(contactUsLoader.load());
        contactUsController =(ContactUsController) contactUsLoader.getController();
        contactUsController.setMain(this);        
    }
    public void switchScene(Screen nextScreen){
        currentScreen = nextScreen;
        switch(currentScreen){
            case LOGIN:
                loginController.clearFields();
                stage.setScene(loginScene);
                break;
            case HOME:
                homeController.fillBookingsTable();
                stage.setScene(homeScene);
                break;  
            case BOOKING:
                bookingController.loadSessions();
                stage.setScene(bookingScene);
                break;
            case MEMBERSHIPS:
                membershipsController.modifyScreen();
                membershipsController.populateMembershipTypes();
                stage.setScene(membershipsScene);
                break;
            case CHECKOUT:
                checkoutController.modifyScreen();
                stage.setScene(checkoutScene);
                break;
            case PROFILE:
                profileController.fillDetails();
                stage.setScene(profileScene);
                break;
            case CONTACT_US:
                contactUsController.setView();
                stage.setScene(contactUsScene);
                break;
            case DASHBOARD:
                stage.setScene(dashboardScene);
                break;
            case MEMBER_VIEW:
                memberViewController.fillMemberTable();
                stage.setScene(memberViewScene);
                break;
            case TRAINER_VIEW:
                trainerViewController.fillTrainerTable();
                stage.setScene(trainerViewScene);
                break;
            case BRANCH_VIEW:
                branchViewController.fillBranchTable();
                stage.setScene(branchViewScene);
                break;
            case EQUIPMENT_VIEW:
                equipmentViewController.fillEquipmentTable();
                stage.setScene(equipmentViewScene);
                break;
            case SESSION_VIEW:
                sessionViewController.fillSessionTable();
                stage.setScene(sessionViewScene);
                break;
            case MEMBERSHIP_TYPE_VIEW:
                membershipTypeViewController.fillMembershipTable();
                stage.setScene(membershipTypeViewScene);
                break;
            case PAYMENT_VIEW:
                paymentViewController.fillPaymentTable();
                stage.setScene(paymentViewScene);
                break;
            case MEMBER_EDIT:
                memberEditController.setScreen();
                stage.setScene(memberEditScene);
                break;
            case TRAINER_ENTRY:
                trainerEntryController.setScreen();
                stage.setScene(trainerEntryScene);
                break;
            case BRANCH_ENTRY:
                branchEntryController.setScreen();
                stage.setScene(branchEntryScene);
                break;
            case EQUPIMENT_ENTRY:
                equipmentEntryController.setScreen();
                stage.setScene(equipmentEntryScene);
                break;
            case SESSION_ENTRY:
                sessionEntryController.setScreen();
                stage.setScene(sessionEntryScene);
                break;
            // case MEMBERSHIP_TYPE_ENTRY:
            //     membershipTypeEntryController.setScreen();
            //     stage.setScene(membershipTypeEntryScene);
            //     break;
            case PAYMENT_ENTRY:
                paymentEntryController.setScreen();
                stage.setScene(paymentEntryScene);
                break;

            default: break;
        }
    } 
//========================================LOGIN========================================
    public boolean login(String phoneNumber, String password) throws Exception {
        if (userDAO.getUserByPhoneNumber(phoneNumber)==null) return false;
        else{
            System.out.println("AAAAAAAAAAAAA");
            String role = userDAO.validateLogin(phoneNumber, password);
            currentUser = userDAO.getUserByPhoneNumber(phoneNumber);
            if(role.equals("Member"))
                {currentMember = memberDAO.getMemberByPhoneNumber(phoneNumber);
                isAdmin=false;}
            
            else if(role.equals("Admin")){
                currentUser= userDAO.getUserByPhoneNumber(phoneNumber);
                isAdmin=true;
            }
            return true;
        }
    }
    public void logout(){
        currentMember = null;
        currentUser = null;
        loginController.clearFields();
        switchScene(Screen.LOGIN);
    }
//========================================MEMBER/GUEST LOGIC========================================
    public void guestCheckout(Double paymentAmount, String firstName, String lastName, String password,
    String phoneNumber, String gender, Date dateOfBirth,int duration,String branchName) throws SQLException{
        Branch branch = branchDAO.getBranchByName(branchName);
        Member member = new Member(
            userDAO.getMaxUserId()+1,
            firstName,
            lastName,
            password,
            phoneNumber,
            gender,
            dateOfBirth,
            selectedMembership.getMembershipTypeID(),
            branch.getBranchID()
        );
        memberDAO.createMember(member,duration,paymentAmount);
        System.out.println("Member Details:");
        System.out.println("User ID: " + member.getUserId());
        System.out.println("First Name: " + member.getFirstName());
        System.out.println("Last Name: " + member.getLastName());
        System.out.println("Phone Number: " + member.getPhoneNumber());
        System.out.println("Gender: " + member.getGender());
        System.out.println("Date of Birth: " + member.getBirthDateAsString());
        System.out.println("Membership Type ID: " + member.getMembershipId());
        System.out.println("Branch ID: " + branch.getBranchID());
        currentMember = memberDAO.getMemberById(member.getUserId());
        isGuest=false;
    }
    public void extendSubscription(int duration,double paymentAmount) throws SQLException{        
        memberDAO.extendSubscription(currentMember.getUserId(), duration, "CreditCard", paymentAmount);
        currentMember = memberDAO.getMemberById(currentMember.getUserId());
    }
    public void freezeSubscription(int duration) throws SQLException{
        System.out.println("Freezing");
        memberDAO.freezeSubscription(currentMember.getUserId(),duration);
        currentMember = memberDAO.getMemberById(currentMember.getUserId());
    }
    public void renewSubscription(int duration,double paymentAmount)throws SQLException{
        memberDAO.renewSubscription(currentMember.getUserId(), duration, selectedMembership.getMembershipTypeID(), "CreditCard", paymentAmount);
        currentMember = memberDAO.getMemberById(currentMember.getUserId());
    }
    public void cancelSubscription()throws SQLException {
        memberDAO.cancelSubscription(currentMember.getUserId());
        currentMember = memberDAO.getMemberById(currentMember.getUserId());
    }
    public void unfreezeSubscription()throws SQLException{
        memberDAO.unfreezeSubscription(currentMember.getUserId());
        currentMember = memberDAO.getMemberById(currentMember.getUserId());
    }
    public void bookSession(int sessionId) throws SQLException{
        bookingDAO.createBooking(currentMember.getUserId(), sessionId);
    }
    public void cancelBooking(int bookingId){
        bookingDAO.cancelBooking(bookingId);
    }
    public void updateMemberCredentials(String phoneNumber, String password) throws SQLException{
        currentMember.setPassword(password);
        currentMember.setPhoneNumber(phoneNumber);
        memberDAO.updateMember(currentMember);
    }
//=======================================Admin Logic===================================================
public void deleteUser(int userId) {
    userDAO.deleteUser(userId);
}
public List<Member> getAllMemberDetails() {
   return memberDAO.getAllMemberDetails();
}
public Member getMemberById(int memberId) {
    return memberDAO.getMemberById(memberId);
}
public void updateMember(Member member) throws SQLException{
    memberDAO.updateMember(member);
}
public void createTrainer(String firstName, String lastName, String phoneNumber, String gender, 
                         String specialization, int experienceYears, double salary, 
                         String status, java.sql.Date dateOfBirth, int branchId) throws SQLException{
    Trainer trainer = new Trainer(userDAO.getMaxUserId()+1,firstName,lastName,"T000000000",phoneNumber,gender,dateOfBirth,specialization,experienceYears,
    salary,branchId,status);
    trainerDAO.addNewTrainer(trainer);
}
public List<Trainer> getAllTrainerDetails() {
    return trainerDAO.getAllTrainerDetails();
}
public void updateTrainer() throws SQLException{
    trainerDAO.updateTrainer(currentTrainer);
}
public void updateMember() throws SQLException{
    memberDAO.updateMember(currentMember);
}
public void deleteBranch(){
    branchDAO.deleteBranch(currentBranch.getBranchID());
}
public void createBranch(String name, String location, String status, int adminID) throws SQLException{
    Branch branch = new Branch(branchDAO.getMaxBranchId() + 1, name, location, status, adminID);
    branchDAO.createBranch(branch);
}
public void updateBranch()throws SQLException{
    branchDAO.updateBranch(currentBranch);
}
public void deleteEquipment() {
    equipmentDAO.deleteEquipment(currentEquipment.getEquipmentID());
}
public void createEquipment(String name, String status,int branchID)throws SQLException{
    Equipment e=new Equipment(equipmentDAO.getMaxEquipmentId()+1, name, null, null, status, branchID);
    equipmentDAO.addNewEquipment(e);
}
public void updateSession() throws SQLException {
    sessionDAO.updateSession(currentSession);
}
public void deleteSession() {
    sessionDAO.deleteSession(currentSession.getSessionID());
}
public void createSession(int trainerID, int branchID, String sessionType, int maxCapacity, LocalDateTime dateTime,
    int duration, String status) throws SQLException {
        Session session = new Session(
            sessionDAO.getMaxSessionId() + 1,
            trainerID,
            branchID,
            sessionType,
            maxCapacity,
            Timestamp.valueOf(dateTime),
            duration,
            status
        );
    sessionDAO.createSession(session);
}

// public void updateMembershipType() throws SQLException {
//     membershipTypeDao.updateMembershipType(currentMembershipType);
// }
// public void deleteMembershipType() {
//     membershipTypeDao.deleteMembershipType(currentMembershipType.getMembershipTypeID());
// }
// public void createMembershipType(String name, double price, int duration, String description) throws SQLException {
//     MembershipType membershipType = new MembershipType(membershipTypeDao.getMaxMembershipTypeId() + 1, name, price, duration, description);
//     membershipTypeDao.addNewMembershipType(membershipType);
// }
public void cancelPayment() {
    paymentDAO.cancelPayment(currentPayment.getPaymentID());
}
public void createPayment(int memberID, String category, String paymentMethod, double amount) throws SQLException {
    paymentDAO.createPayment(memberID, category, paymentMethod, amount);
}

public void updateEquipment()throws SQLException{
    equipmentDAO.updateEquipment(currentEquipment);
}
public Branch getBranchByName(String branchName) {
    return branchDAO.getBranchByName(branchName);
}
public List<Branch> getAllBranches(){
    return branchDAO.getBranches();
}
public List<Equipment> getAllEquipment(){
    return equipmentDAO.getEquipment();
}    
public List<Payment> getAllPayments(){
    return paymentDAO.getAllPayments();
}
public List<Session> getAllSessions(){
    return sessionDAO.getAllSessions();
}
//========================================Member Getters/Setters===================================================
    public List<Booking> getAllMemberBookings(){
        return bookingDAO.getAllMemberBookings(currentMember.getUserId());
    }
    public Member getCurrentMember(){
        return currentMember;
    }
    public List<MembershipType> getAllMembershipTypes(){
        return membershipTypeDao.getAllMembershipTypes();
    }
    public String getMembershipName(int membershipId){
        return membershipTypeDao.getMembershipTypeName(membershipId);
    } 
    public Trainer getTrainerByID(int trainerId){
        return trainerDAO.getTrainerById(trainerId);
    }
    public Branch getBranchByID(int branchId){
        return branchDAO.getBranchById(branchId);
    }

    public boolean isGuest(){
        return isGuest;
    }
    public void setIsGuest(boolean isGuest){
        this.isGuest = isGuest;
    }
    public boolean isAdmin(){
        return isAdmin;
    }
    public void setSelectedMembership(MembershipType membershipType){
        this.selectedMembership = membershipType;
    }
    public MembershipType getSelectedMembership(){  
        return selectedMembership;
    }
    public boolean isFreezing(){
        return isFreezing;
    }
    public void setIsFreezing(boolean isFreezing){
        this.isFreezing = isFreezing;
    }
    public boolean isExtending(){
        return isExtending;
    }
    public void setIsExtending(boolean isExtending){
        this.isExtending = isExtending;
    }
    public void setSelectedMembership(){
        if(!isGuest)selectedMembership=membershipTypeDao.getMembershipTypeById(currentMember.getMembershipId());
    }
    public Session getEarliestSessionByType(String sessionType){
        return sessionDAO.getEarliestSessionByType(sessionType);
    }
    public Session getSessionByID(int sessionID){
        return sessionDAO.getSessionByID(sessionID);
    }
    public Trainer getCurrentTrainer() {
        return currentTrainer;
    }
    public void setCurrentTrainer(Trainer currentTrainer) {
        if(currentTrainer!=null)
            this.currentTrainer = trainerDAO.getTrainerById(currentTrainer.getUserId());
        else this.currentTrainer=null;
    }
    public void setCurrentMember(Member member) {
        if(member!=null)
            this.currentMember = memberDAO.getMemberById(member.getUserId());
        else this.currentMember=null;
    }
    public void setCurrentUser(User user) {
        if(currentUser!=null)
            this.currentUser = userDAO.getUserByPhoneNumber(user.getPhoneNumber());
        else this.currentUser=null;
    }
    public Branch getCurrentBranch() {
        return currentBranch;
    }
    public void setCurrentBranch(Branch currentBranch) {
        this.currentBranch = currentBranch;
    }
    public Equipment getCurrentEquipment() {
        return currentEquipment;
    }
    public void setCurrentEquipment(Equipment currentEquipment) {
        this.currentEquipment = currentEquipment;
    }
    public Session getCurrentSession() {
        return currentSession;
    }
    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
    public MembershipType getCurrentMembershipType() {
        return currentMembershipType;
    }
    public void setCurrentMembershipType(MembershipType currentMembershipType) {
        this.currentMembershipType = currentMembershipType;
    }
    public Payment getCurrentPayment() {
        return currentPayment;
    }
    public void setCurrentPayment(Payment currentPayment) {
        this.currentPayment = currentPayment;
    }
    private Stage stage;
    private Screen currentScreen;

    private Scene dashboardScene;
    private DashboardController dashboardController;

    private Scene loginScene;
    private LoginController loginController;

    private Scene homeScene;
    private HomeController homeController;

    private Scene bookingScene;
    private BookingController bookingController;

    private Scene membershipsScene;
    private MembershipsController membershipsController;

    private Scene checkoutScene;
    private CheckoutController checkoutController;

    private Scene profileScene;
    private ProfileController profileController;

    private Scene contactUsScene;
    private ContactUsController contactUsController;

    private Scene memberViewScene;
    private MemberViewController memberViewController;
    
    private Scene trainerViewScene;
    private TrainerViewController trainerViewController;
    
    private Scene branchViewScene;
    private BranchViewController branchViewController;
    
    private Scene equipmentViewScene;
    private EquipmentViewController equipmentViewController;
    
    private Scene sessionViewScene;
    private SessionViewController sessionViewController;
    
    private Scene membershipTypeViewScene;
    private MembershipTypeViewController membershipTypeViewController;
    
    private Scene paymentViewScene;
    private PaymentViewController paymentViewController;    

    private Scene memberEditScene;
    private MemberEditController memberEditController;

    private Scene trainerEntryScene;
    private TrainerEntryController trainerEntryController;

    private Scene branchEntryScene;
    private BranchEntryController branchEntryController;

    private Scene equipmentEntryScene;
    private EquipmentEntryController equipmentEntryController;

    private Scene sessionEntryScene;
    private SessionEntryController sessionEntryController;

    // private Scene membershipTypeEntryScene;
    // private MembershipTypeEntryController membershipTypeEntryController;

    private Scene paymentEntryScene;
    private PaymentEntryController paymentEntryController;
}
