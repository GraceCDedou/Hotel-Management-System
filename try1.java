import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class try1 extends JFrame {
    private static final Color WINE = new Color(76, 8, 31);
    private static final Color GOLD = new Color(205, 169, 91);
    private static final Color CREAM = new Color(250, 247, 235);
    private static final Color INK = new Color(55, 42, 35);
    private static final Path DATA = Paths.get(System.getProperty("user.home"), ".imperial-solis");

    // Authorized Admin Credentials
    private static final Map<String, String> ADMINS = Map.of(
        "Grace", "admingd@1245W",
        "Kc", "adminkc@1516X",
        "Michelle", "admintm@1652S",
        "Inam", "adminia@1824K",
        "Rafiq", "adminrq@1471N",
        "Michele", "adminkm@1733Z",
        "Peloyame", "adminkp@1368Q"
    );

    // Rooms taken from the Imperial Solis website
    private final List<Room> rooms = Arrays.asList(
        new Room("Deluxe King Room", 820, 2, "Spacious and serene, featuring panoramic views, a king-size bed, soaking tub, high-speed Wi-Fi, and mini bar."),
        new Room("Imperial Suite", 850, 4, "Flagship suite with floor-to-ceiling windows, private terrace, king bed, jacuzzi, and 24/7 butler service."),
        new Room("Solis Grand Room", 1250, 3, "Elevated luxury with twin seating areas, walk-in wardrobe, king bed, smart TV, and oversized marble bath."),
        new Room("Executive Twin Room", 980, 4, "Refined comfort suited for business travelers or companions with two queen beds, workspace, high-speed Wi-Fi, and mini bar."),
        new Room("Penthouse Suite", 1400, 4, "Sprawling two-level suite with a king bed, private rooftop lounge, infinity bath, and dedicated chef."),
        new Room("Garden View Room", 780, 2, "Tranquil escape with lush garden vistas, classic furnishings, queen bed, Wi-Fi, and coffee station."),
        new Room("Kiddies Room (Add-on)", 300, 2, "Available in premium rooms — adjoining kids' room with single/bunk bed, beach view, Wi-Fi, and play area.")
    );

    // 4 Special Offers synced with the Imperial Solis website
    private final List<SpecialOffer> specialOffers = Arrays.asList(
        new SpecialOffer(
            "Stay 3 Nights, Pay for 2",
            "Save 20% • Extended Luxury",
            "R 1,640.00 / 3-night package",
            "Unwind longer in our Deluxe King Room or Imperial Suite with a complimentary third night, daily Continental Morning breakfast for two, and 2:00 PM late checkout.",
            0
        ),
        new SpecialOffer(
            "Imperial Romance & Spa Retreat",
            "Most Popular • Couples Escape",
            "R 1,350.00 / night",
            "An intimate escape featuring a rose-petal suite turndown, signature Rose Petal Martinis, 60-min couples warm stone massage, and private rooftop jacuzzi access.",
            1
        ),
        new SpecialOffer(
            "Kids Stay & Dine Free",
            "Family Deal • Solis Family Getaway",
            "R 1,150.00 / night",
            "Book any suite and receive an adjoining Kiddies Room included, complimentary Junior Menu meals for kids under 12, and full supervised Kids' Club & WaterSlide access.",
            6
        ),
        new SpecialOffer(
            "Advance Purchase Suite Escape",
            "Save 15% • Early Bird Privilege",
            "From R 663.00 / night",
            "Reserve your stay at least 14 days in advance to enjoy 15% off our best available room rates, complimentary private chauffeur airport transfers, and priority upgrade.",
            5
        )
    );

    // Exact Food, Drinks, Kids' Club & Amenities synced with index.html
    private final List<ExtraItem> extraMenu = Arrays.asList(
        new ExtraItem("Breakfast", "Continental Morning", 180, "Full mezze breakfast spread with freshly baked pastries, seasonal fruit, and artisan coffee."),
        new ExtraItem("Breakfast", "The Imperial Spread", 220, "Mediterranean starter plate with artisan dips, poached eggs, and warm sourdough toast."),
        new ExtraItem("Main", "Braised Short Rib", 320, "Slow-braised beef served with fragrant herb rice and signature reduction."),
        new ExtraItem("Main", "Rotisserie Spiced Chicken", 260, "Whole flame-roasted herb & peri-spiced chicken with roasted baby potatoes."),
        new ExtraItem("Main", "Seared Tuna Salad", 240, "Sesame-crusted seared tuna over crisp garden greens with citrus vinaigrette."),
        new ExtraItem("Kids Meal", "Burger & Fries", 110, "Juicy mini beef or chicken burger served with crispy golden fries."),
        new ExtraItem("Plant-Based", "Heritage Spice Bowl", 195, "Spiced tomato, roasted heirloom vegetables, chickpeas, and fragrant grains."),
        new ExtraItem("Cocktail", "Rose Petal Martini", 140, "Botanical gin infused with Damascus rose water, lychee, and edible petals."),
        new ExtraItem("Dessert", "Acai Bliss Bowl", 120, "Chilled organic acai berry bowl topped with toasted coconut, berries, and honey."),
        new ExtraItem("Dessert", "Warm Syrniki", 130, "Golden cottage cheese pancakes dusted with vanilla sugar and fresh forest berries."),
        new ExtraItem("Dessert", "Solara Matcha Cream Cake", 115, "Delicate ceremonial-grade matcha sponge layered with whipped mascarpone cream."),
        new ExtraItem("Kids Dessert", "Vanilla Cream Cupcake", 65, "Fun vanilla bean cupcake swirled with pastel buttercream for young and old."),
        new ExtraItem("Amenity • Pool", "Outdoor Infinity Pool Day Pass", 150, "Sun-drenched pool deck with premium loungers and swim-up bar access."),
        new ExtraItem("Amenity • Fitness", "Fitness Centre Session", 120, "State-of-the-art equipment, weight racks, and panoramic city views."),
        new ExtraItem("Amenity • Wellness", "Yoga & Pilates Studio Class", 180, "Group session led by certified instructors in our garden-facing studio."),
        new ExtraItem("Amenity • Spa", "Traditional Sauna Session", 250, "Authentic Finnish wood sauna with birch fragrance and therapeutic steam."),
        new ExtraItem("Amenity • Rooftop", "Rooftop Jacuzzi (Private Hour)", 350, "Seamless-edge hot tub at sunset overlooking the city skyline."),
        new ExtraItem("Amenity • Events", "Grand Ballroom Event Access", 2500, "Crystal chandeliers, grand stage, and banquet seating access."),
        new ExtraItem("Entertainment", "Private Cinema Screening", 220, "80-seat screening room with Dolby Atmos surround sound and in-seat service."),
        new ExtraItem("Entertainment", "Imperial Solis Bar VIP Table", 200, "Luxury bar table reservation with live music and ambient lighting."),
        new ExtraItem("Kids' Club", "Kids' Club Full Day Pass", 150, "Adventure Playroom, WaterSlide, Indoor Soft-Play, Children's Theatre & Jumping Castle.")
    );

    private final List<Booking> bookings = new ArrayList<>();
    private final List<Booking> displayedBookings = new ArrayList<>();
    private String currentUser = "Guest";
    private JLabel welcomeLabel;
    private DefaultListModel<String> bookingListModel = new DefaultListModel<>();
    private JList<String> bookingList;
    private JTextField searchField;
    private JTextField guestNameField;
    private JTextField guestEmailField;
    private JComboBox<String> roomChoice;
    private JSpinner guestsSpinner;
    private JTextField checkInField, checkOutField;
    private JTextArea roomDescription;
    private JLabel totalLabel;
    private Booking lastBooking;
    private JTabbedPane tabs;

    // Extras & Orders Tab Controls
    private JComboBox<String> orderBookingChoice;
    private JTextArea orderSummaryArea;

    public try1() {
        super("The Imperial Solis | Hotel & Resort");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 740);
        setMinimumSize(new Dimension(920, 640));
        setLocationRelativeTo(null);
        loadBookings();
        showWelcome();
    }

    private void showWelcome() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(WINE);
        JPanel hero = new JPanel();
        hero.setBackground(WINE);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBorder(new EmptyBorder(65, 70, 55, 70));

        JLabel crest = new JLabel("♛  S  ♛");
        crest.setForeground(GOLD); crest.setFont(new Font("Serif", Font.BOLD, 42));
        crest.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel name = new JLabel("THE IMPERIAL SOLIS");
        name.setForeground(GOLD); name.setFont(new Font("Serif", Font.BOLD, 34));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("HOTEL & RESORT");
        sub.setForeground(CREAM); sub.setFont(new Font("SansSerif", Font.PLAIN, 15));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel tagline = new JLabel("Luxury Stays, Unforgettable Moments");
        tagline.setForeground(CREAM); tagline.setFont(new Font("Serif", Font.ITALIC, 19));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel imageHint = new JLabel("✦  WELCOME TO A WORLD OF TRUE LUXURY  ✦");
        imageHint.setForeground(GOLD); imageHint.setFont(new Font("SansSerif", Font.BOLD, 14));
        imageHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = goldButton("GET STARTED");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> showAuth());
        hero.add(Box.createVerticalGlue()); hero.add(crest); hero.add(Box.createVerticalStrut(18));
        hero.add(name); hero.add(sub); hero.add(Box.createVerticalStrut(22));
hero.add(tagline); hero.add(Box.createVerticalStrut(55)); hero.add(imageHint);
hero.add(Box.createVerticalStrut(55)); hero.add(start); hero.add(Box.createVerticalGlue());
root.add(hero, BorderLayout.CENTER);
setContentPane(root); revalidate(); repaint();
}
private void showAuth() {
JPanel panel = new JPanel(new GridBagLayout());
panel.setBackground(CREAM);
GridBagConstraints g = new GridBagConstraints();
g.insets = new Insets(9, 12, 9, 12); g.fill = GridBagConstraints.HORIZONTAL; g.gridx = 0;
JLabel crest = new JLabel("♛  S  ♛", SwingConstants.CENTER);
crest.setFont(new Font("Serif", Font.BOLD, 34)); crest.setForeground(WINE);
JLabel title = new JLabel("THE IMPERIAL SOLIS", SwingConstants.CENTER);
title.setFont(new Font("Serif", Font.BOLD, 22)); title.setForeground(WINE);
JTextField username = field("");
username.setToolTipText("Admin Name");
JPasswordField password = new JPasswordField(); styleField(password); password.setToolTipText("Password");
JButton login = wineButton("ADMIN LOGIN");
g.gridy=0; panel.add(crest,g); g.gridy++; panel.add(title,g);
g.gridy++; panel.add(label("Admin Name"), g);
g.gridy++; panel.add(username,g);
g.gridy++; panel.add(label("Password"), g);
g.gridy++; panel.add(password,g);
g.gridy++; panel.add(login,g);
login.addActionListener(e -> {
String u = username.getText().trim();
String p = new String(password.getPassword());
if (u.isEmpty() || p.isEmpty()) {
message("Enter your admin name and password to continue.");
return;
}
if (ADMINS.containsKey(u) && ADMINS.get(u).equals(p)) {
currentUser = u;
loadBookings();
showDashboard();
} else {
message("Access denied. Invalid admin name or password.");
password.setText("");
}
});
setContentPane(centered(panel)); revalidate(); repaint();
}
private void showDashboard() {
JPanel root = new JPanel(new BorderLayout());
root.setBackground(CREAM);
JPanel header = new JPanel(new BorderLayout());
header.setBackground(WINE); header.setBorder(new EmptyBorder(14, 22, 14, 22));
JLabel brand = new JLabel("♛  THE IMPERIAL SOLIS  |  HOTEL & RESORT");
brand.setForeground(GOLD); brand.setFont(new Font("Serif", Font.BOLD, 20));
welcomeLabel = new JLabel("Welcome, " + currentUser + "   ");
welcomeLabel.setForeground(CREAM);
header.add(brand, BorderLayout.WEST); header.add(welcomeLabel, BorderLayout.EAST);
root.add(header, BorderLayout.NORTH);
tabs = new JTabbedPane();
tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
tabs.addTab("Home", homePanel(tabs));
tabs.addTab("Available Rooms", roomsPanel());
tabs.addTab("Special Offers", offersPanel());
tabs.addTab("Book a Room", bookingPanel());
tabs.addTab("Extras & Orders", extrasPanel());
tabs.addTab("Bookings", bookingsPanel());
tabs.addTab("Notifications", notificationsPanel());
root.add(tabs, BorderLayout.CENTER);
JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
footer.setBackground(CREAM);
JButton logout = wineButton("LOG OUT");
logout.addActionListener(e -> showWelcome());
footer.add(logout); root.add(footer, BorderLayout.SOUTH);
setContentPane(root); revalidate(); repaint();
}
private JPanel homePanel(JTabbedPane tabs) {
JPanel p = new JPanel(new GridBagLayout()); p.setBackground(CREAM);
JPanel content = new JPanel(); content.setBackground(CREAM);
content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
JLabel heading = new JLabel("Experience True Luxury");
heading.setFont(new Font("Serif", Font.BOLD, 32)); heading.setForeground(WINE);
JLabel tagline = new JLabel("A refined stay. Exceptional comfort. Unforgettable moments.");
tagline.setFont(new Font("Serif", Font.ITALIC, 17)); tagline.setForeground(INK);
content.add(heading); content.add(Box.createVerticalStrut(10)); content.add(tagline);
content.add(Box.createVerticalStrut(38));
JPanel cards = new JPanel(new GridLayout(2,2,18,18)); cards.setBackground(CREAM);
List labels = Arrays.asList("AVAILABLE ROOMS", "BOOKINGS", "SPECIAL OFFERS", "EXTRAS & ORDERS");
List desc = Arrays.asList("Explore our available luxury rooms", "Search, add, or cancel reservations", "Discover 4 seasonal packages", "Add dining, drinks & amenities");
List targetTabs = Arrays.asList(1, 5, 2, 4);
for (int i = 0; i < labels.size(); i++) {
final int tabIdx = (int) targetTabs.get(i);
JPanel card = new JPanel(new BorderLayout(5,8)); card.setBackground(Color.WHITE);
card.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true), new EmptyBorder(20,20,20,20)));
card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
JLabel l = new JLabel((String) labels.get(i), SwingConstants.CENTER); l.setForeground(WINE);
l.setFont(new Font("SansSerif",Font.BOLD,17));
JLabel d = new JLabel((String) desc.get(i), SwingConstants.CENTER); d.setForeground(INK);
card.add(l,BorderLayout.CENTER); card.add(d,BorderLayout.SOUTH);
card.addMouseListener(new java.awt.event.MouseAdapter() {
public void mouseClicked(java.awt.event.MouseEvent e) { tabs.setSelectedIndex(tabIdx); }
});
cards.add(card);
}
content.add(cards);
JButton book = goldButton("BOOK YOUR STAY");
book.addActionListener(e -> tabs.setSelectedIndex(3));
content.add(Box.createVerticalStrut(30)); content.add(book);
p.add(content); return p;
}
private JPanel roomsPanel() {
JPanel p = new JPanel(new BorderLayout(15,15)); p.setBackground(CREAM);
p.setBorder(new EmptyBorder(22,25,22,25));
JLabel title = sectionTitle("AVAILABLE ROOMS");
p.add(title,BorderLayout.NORTH);
JPanel list = new JPanel(new GridLayout(0,1,12,12)); list.setBackground(CREAM);
if (rooms.isEmpty()) {
JLabel empty = new JLabel("No rooms currently available.", SwingConstants.CENTER);
empty.setFont(new Font("SansSerif", Font.ITALIC, 16));
empty.setForeground(INK);
list.add(empty);
} else {
for (int i = 0; i < rooms.size(); i++) {
Room r = rooms.get(i);
final int roomIndex = i;
JPanel card = new JPanel(new BorderLayout(15,8)); card.setBackground(Color.WHITE);
card.setBorder(new CompoundBorder(new LineBorder(new Color(220,210,185),1,true),new EmptyBorder(17,20,17,20)));
JLabel name = new JLabel(r.name); name.setFont(new Font("Serif",Font.BOLD,21)); name.setForeground(WINE);
JLabel price = new JLabel("R " + money(r.price) + " / night  •  Up to " + r.capacity + " guests");
price.setFont(new Font("SansSerif",Font.BOLD,14)); price.setForeground(INK);
JLabel desc = new JLabel(r.description);
JPanel text = new JPanel(new GridLayout(3,1)); text.setOpaque(false);
text.add(name); text.add(price); text.add(desc);
JButton select = goldButton("BOOK NOW");
select.addActionListener(e -> {
if (roomChoice != null) roomChoice.setSelectedIndex(roomIndex);
if (tabs != null) tabs.setSelectedIndex(3);
});
card.add(text,BorderLayout.CENTER); card.add(select,BorderLayout.EAST); list.add(card);
}
}
JScrollPane scroll = new JScrollPane(list);
scroll.setBorder(null);
scroll.getVerticalScrollBar().setUnitIncrement(16);
p.add(scroll, BorderLayout.CENTER);
return p;
}
private JPanel offersPanel() {
JPanel p = new JPanel(new BorderLayout(15,15)); p.setBackground(CREAM);
p.setBorder(new EmptyBorder(22,25,22,25));
p.add(sectionTitle("SPECIAL OFFERS"), BorderLayout.NORTH);
JPanel list = new JPanel(new GridLayout(0,1,14,14)); list.setBackground(CREAM);
for (SpecialOffer offer : specialOffers) {
JPanel card = new JPanel(new BorderLayout(15,8)); card.setBackground(Color.WHITE);
card.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true), new EmptyBorder(18,22,18,22)));
JLabel badge = new JLabel(offer.badge.toUpperCase());
badge.setFont(new Font("SansSerif", Font.BOLD, 11));
badge.setForeground(new Color(145, 105, 35));
JLabel title = new JLabel(offer.title);
title.setFont(new Font("Serif", Font.BOLD, 21));
title.setForeground(WINE);
JLabel price = new JLabel(offer.priceLabel);
price.setFont(new Font("SansSerif", Font.BOLD, 14));
price.setForeground(INK);
JTextArea desc = new JTextArea(offer.description);
desc.setLineWrap(true);
desc.setWrapStyleWord(true);
desc.setEditable(false);
desc.setOpaque(false);
desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
desc.setForeground(INK);
JPanel text = new JPanel();
text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
text.setOpaque(false);
badge.setAlignmentX(Component.LEFT_ALIGNMENT);
title.setAlignmentX(Component.LEFT_ALIGNMENT);
price.setAlignmentX(Component.LEFT_ALIGNMENT);
desc.setAlignmentX(Component.LEFT_ALIGNMENT);
text.add(badge);
text.add(Box.createVerticalStrut(3));
text.add(title);
text.add(Box.createVerticalStrut(4));
text.add(price);
text.add(Box.createVerticalStrut(6));
text.add(desc);
JButton claim = wineButton("CLAIM OFFER");
claim.addActionListener(e -> {
if (roomChoice != null && offer.defaultRoomIndex < rooms.size()) {
roomChoice.setSelectedIndex(offer.defaultRoomIndex);
}
if (tabs != null) tabs.setSelectedIndex(3);
message("Offer selected: " + offer.title + "\nEnter Guest name, email, and dates below to reserve.");
});
JPanel btnWrap = new JPanel(new GridBagLayout());
btnWrap.setOpaque(false);
btnWrap.add(claim);
card.add(text, BorderLayout.CENTER);
card.add(btnWrap, BorderLayout.EAST);
list.add(card);
}
JScrollPane scroll = new JScrollPane(list);
scroll.setBorder(null);
scroll.getVerticalScrollBar().setUnitIncrement(16);
p.add(scroll, BorderLayout.CENTER);
return p;
}
// Compact 2-column layout with Guest Name, Guest Email, Room Type, Dates, Guests & Button all on one page
private JPanel bookingPanel() {
JPanel p = new JPanel(new GridBagLayout()); p.setBackground(CREAM);
JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
form.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true),new EmptyBorder(16,26,16,26)));
GridBagConstraints g = new GridBagConstraints();
g.insets = new Insets(4, 8, 4, 8);
g.fill = GridBagConstraints.HORIZONTAL;
g.weightx = 0.5;
g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
form.add(sectionTitle("BOOKING FLOW"), g);
g.gridwidth = 1; g.gridy++;
g.gridx = 0; form.add(label("Guest Name"), g);
g.gridx = 1; form.add(label("Guest Email"), g);
g.gridy++;
g.gridx = 0;
guestNameField = compactField("");
guestNameField.setToolTipText("Enter guest name");
form.add(guestNameField, g);
g.gridx = 1;
guestEmailField = compactField("");
guestEmailField.setToolTipText("Enter guest email address");
form.add(guestEmailField, g);
g.gridy++;
g.gridx = 0; form.add(label("Room Type"), g);
g.gridx = 1; form.add(label("Number of guests"), g);
g.gridy++;
g.gridx = 0;
List roomNameList = new ArrayList<>();
for (Room r : rooms) roomNameList.add(r.name);
roomChoice = new JComboBox<>(new Vector<>(roomNameList));
styleCombo(roomChoice);
form.add(roomChoice, g);
g.gridx = 1;
guestsSpinner = new JSpinner(new SpinnerNumberModel(2,1,8,1));
guestsSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
form.add(guestsSpinner, g);
g.gridy++;
g.gridx = 0; form.add(label("Check-in date (YYYY-MM-DD)"), g);
g.gridx = 1; form.add(label("Check-out date (YYYY-MM-DD)"), g);
g.gridy++;
g.gridx = 0; checkInField = compactField(LocalDate.now().plusDays(1).toString()); form.add(checkInField, g);
g.gridx = 1; checkOutField = compactField(LocalDate.now().plusDays(3).toString()); form.add(checkOutField, g);
g.gridy++; g.gridx = 0; g.gridwidth = 2;
roomDescription = new JTextArea(2, 42);
roomDescription.setLineWrap(true);
roomDescription.setWrapStyleWord(true);
roomDescription.setEditable(false);
roomDescription.setBackground(CREAM);
roomDescription.setFont(new Font("Monospaced", Font.PLAIN, 12));
roomDescription.setBorder(new EmptyBorder(6, 10, 6, 10));
roomDescription.setText(rooms.get(0).description);
form.add(roomDescription, g);
g.gridy++;
totalLabel = new JLabel("Estimated total: R 0.00");
totalLabel.setForeground(WINE);
totalLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
form.add(totalLabel, g);
g.gridy++;
JButton pay = wineButton("CONTINUE TO PAYMENT");
form.add(pay, g);
roomChoice.addActionListener(e -> updateEstimate());
checkInField.addActionListener(e -> updateEstimate());
checkOutField.addActionListener(e -> updateEstimate());
guestsSpinner.addChangeListener(e -> updateEstimate());
pay.addActionListener(e -> processBooking());
updateEstimate();
p.add(form);
return p;
}
// Extras & Orders Panel with PRINT SLIP + CLEAR EXTRAS ON BOOKING
private JPanel extrasPanel() {
JPanel p = new JPanel(new BorderLayout(14, 14));
p.setBackground(CREAM);
p.setBorder(new EmptyBorder(20, 25, 20, 25));
JPanel topBar = new JPanel(new BorderLayout(12, 0));
topBar.setBackground(CREAM);
topBar.add(sectionTitle("EXTRAS & GUEST ORDERS"), BorderLayout.WEST);
JPanel selectWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
selectWrap.setBackground(CREAM);
selectWrap.add(label("Select Guest Booking:"));
orderBookingChoice = new JComboBox<>();
styleCombo(orderBookingChoice);
orderBookingChoice.setPreferredSize(new Dimension(340, 34));
orderBookingChoice.addActionListener(e -> updateOrderSummary());
selectWrap.add(orderBookingChoice);
topBar.add(selectWrap, BorderLayout.EAST);
p.add(topBar, BorderLayout.NORTH);
JPanel menuList = new JPanel(new GridLayout(0, 1, 10, 10));
menuList.setBackground(CREAM);
for (ExtraItem item : extraMenu) {
JPanel card = new JPanel(new BorderLayout(12, 6));
card.setBackground(Color.WHITE);
card.setBorder(new CompoundBorder(new LineBorder(new Color(220, 210, 185), 1, true), new EmptyBorder(12, 16, 12, 16)));
JLabel cat = new JLabel(item.category.toUpperCase());
cat.setFont(new Font("SansSerif", Font.BOLD, 10));
cat.setForeground(new Color(145, 105, 35));
JLabel name = new JLabel(item.name + "  —  R " + money(item.price));
name.setFont(new Font("Serif", Font.BOLD, 18));
name.setForeground(WINE);
JLabel desc = new JLabel(item.description);
desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
desc.setForeground(INK);
JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
info.setOpaque(false);
info.add(cat);
info.add(name);
info.add(desc);
JSpinner qtySpin = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
qtySpin.setFont(new Font("SansSerif", Font.PLAIN, 13));
JButton addBtn = goldButton("ADD TO BOOKING");
addBtn.addActionListener(e -> addExtraToSelectedBooking(item, (Integer) qtySpin.getValue()));
JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
rightControls.setOpaque(false);
rightControls.add(label("Qty:"));
rightControls.add(qtySpin);
rightControls.add(addBtn);
card.add(info, BorderLayout.CENTER);
card.add(rightControls, BorderLayout.EAST);
menuList.add(card);
}
JScrollPane menuScroll = new JScrollPane(menuList);
menuScroll.setBorder(null);
menuScroll.getVerticalScrollBar().setUnitIncrement(16);
JPanel receiptCard = new JPanel(new BorderLayout(8, 8));
receiptCard.setBackground(Color.WHITE);
receiptCard.setBorder(new CompoundBorder(new LineBorder(GOLD, 1, true), new EmptyBorder(16, 18, 16, 18)));
receiptCard.setPreferredSize(new Dimension(350, 0));
JLabel recTitle = new JLabel("BOOKING BILL & EXTRAS");
recTitle.setFont(new Font("Serif", Font.BOLD, 18));
recTitle.setForeground(WINE);
receiptCard.add(recTitle, BorderLayout.NORTH);
orderSummaryArea = new JTextArea();
orderSummaryArea.setEditable(false);
orderSummaryArea.setLineWrap(true);
orderSummaryArea.setWrapStyleWord(true);
orderSummaryArea.setBackground(CREAM);
orderSummaryArea.setForeground(INK);
orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
orderSummaryArea.setBorder(new EmptyBorder(12, 12, 12, 12));
receiptCard.add(new JScrollPane(orderSummaryArea), BorderLayout.CENTER);
// Receipt Footer Buttons: PRINT SLIP + CLEAR EXTRAS
JPanel receiptButtons = new JPanel(new GridLayout(1, 2, 8, 0));
receiptButtons.setOpaque(false);
JButton printSlipBtn = goldButton("PRINT SLIP");
printSlipBtn.addActionListener(e -> printSelectedBookingSlip());
JButton clearExtrasBtn = wineButton("CLEAR EXTRAS");
clearExtrasBtn.addActionListener(e -> clearExtrasOnSelectedBooking());
receiptButtons.add(printSlipBtn);
receiptButtons.add(clearExtrasBtn);
receiptCard.add(receiptButtons, BorderLayout.SOUTH);
p.add(menuScroll, BorderLayout.CENTER);
p.add(receiptCard, BorderLayout.EAST);
refreshOrderBookingDropdown();
return p;
}
// Builds, previews, prints, or saves a clean guest bill slip for the selected booking
private void printSelectedBookingSlip() {
if (bookings.isEmpty() || orderBookingChoice == null || orderBookingChoice.getSelectedIndex() < 0) {
message("Please select a guest booking first to print a slip.");
return;
}
Booking b = bookings.get(orderBookingChoice.getSelectedIndex());
double roomOnly = b.total - b.extrasTotal;
String printedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
StringBuilder slipText = new StringBuilder();
slipText.append("================================================\n");
slipText.append("          THE IMPERIAL SOLIS HOTEL & RESORT     \n");
slipText.append("             OFFICIAL GUEST BILL SLIP           \n");
slipText.append("================================================\n");
slipText.append("Printed At   : ").append(printedAt).append("\n");
slipText.append("Issued By    : ").append(currentUser).append("\n");
slipText.append("Reference #  : ").append(b.id).append("\n");
slipText.append("------------------------------------------------\n");
slipText.append("Guest Name   : ").append(b.guest).append("\n");
slipText.append("Guest Email  : ").append(b.email.isEmpty() ? "N/A" : b.email).append("\n");
slipText.append("Room Type    : ").append(b.room).append("\n");
slipText.append("Check-in     : ").append(b.in).append("\n");
slipText.append("Check-out    : ").append(b.out).append("\n");
slipText.append("Guests       : ").append(b.guests).append("\n");
slipText.append("Payment Mode : ").append(b.payment).append("\n");
slipText.append("------------------------------------------------\n");
slipText.append("ORDERED EXTRAS & AMENITIES:\n");
if (b.extrasSummary == null || b.extrasSummary.isEmpty()) {
slipText.append("  • No extras ordered\n");
} else {
for (String itemLine : b.extrasSummary.split("; ")) {
slipText.append("  • ").append(itemLine).append("\n");
}
}
slipText.append("------------------------------------------------\n");
slipText.append(String.format("Room Charges : R %s%n", money(roomOnly)));
slipText.append(String.format("Extras Total : R %s%n", money(b.extrasTotal)));
slipText.append("================================================\n");
slipText.append(String.format("GRAND TOTAL  : R %s%n", money(b.total)));
slipText.append("================================================\n");
slipText.append("   Thank you for staying at The Imperial Solis  \n");
JTextArea previewArea = new JTextArea(slipText.toString(), 24, 50);
previewArea.setEditable(false);
previewArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
previewArea.setBackground(CREAM);
previewArea.setForeground(INK);
previewArea.setBorder(new EmptyBorder(14, 16, 14, 16));
Object[] options = {"SEND TO PRINTER", "SAVE SLIP (.TXT)", "CLOSE"};
int choice = JOptionPane.showOptionDialog(
this,
new JScrollPane(previewArea),
"Print Slip — Ref: " + b.id,
JOptionPane.YES_NO_CANCEL_OPTION,
JOptionPane.PLAIN_MESSAGE,
null,
options,
options[0]
);
if (choice == 0) {
try {
boolean printed = previewArea.print();
if (printed) {
message("Slip sent to printer successfully.");
}
} catch (Exception ex) {
message("Could not open printer: " + ex.getMessage());
}
} else if (choice == 1) {
try {
Files.createDirectories(DATA);
Path slipFile = DATA.resolve("Slip_" + b.id + ".txt");
Files.writeString(slipFile, slipText.toString());
message("Slip saved to:\n" + slipFile.toAbsolutePath());
} catch (IOException ex) {
message("Could not save slip file: " + ex.getMessage());
}
}
}
private void refreshOrderBookingDropdown() {
if (orderBookingChoice == null) return;
int prevIdx = orderBookingChoice.getSelectedIndex();
orderBookingChoice.removeAllItems();
if (bookings.isEmpty()) {
orderBookingChoice.addItem("No bookings available — book a room first");
} else {
for (Booking b : bookings) {
orderBookingChoice.addItem(b.id + " - " + b.guest + " (" + b.room + ")");
}
if (prevIdx >= 0 && prevIdx < bookings.size()) {
orderBookingChoice.setSelectedIndex(prevIdx);
} else {
orderBookingChoice.setSelectedIndex(bookings.size() - 1);
}
}
updateOrderSummary();
}
private void updateOrderSummary() {
if (orderSummaryArea == null) return;
if (bookings.isEmpty() || orderBookingChoice == null || orderBookingChoice.getSelectedIndex() < 0) {
orderSummaryArea.setText("No active booking selected.\n\nCreate a reservation in 'Book a Room' first, then select the guest here to add dining, drinks, or amenities.");
return;
}
Booking b = bookings.get(orderBookingChoice.getSelectedIndex());
double roomOnly = b.total - b.extrasTotal;
String extrasDisplay = (b.extrasSummary == null || b.extrasSummary.isEmpty())
? "  None added yet"
: "  • " + b.extrasSummary.replace("; ", "\n  • ");
orderSummaryArea.setText(
"Reference : " + b.id + "\n" +
"Guest     : " + b.guest + "\n" +
"Email     : " + b.email + "\n" +
"Room      : " + b.room + "\n" +
"Dates     : " + b.in + " to " + b.out + "\n" +
"------------------------------------\n" +
"Room Base : R " + money(roomOnly) + "\n" +
"Extras    : R " + money(b.extrasTotal) + "\n" +
"------------------------------------\n" +
"NEW TOTAL : R " + money(b.total) + "\n\n" +
"Ordered Items:\n" + extrasDisplay
);
}
private void addExtraToSelectedBooking(ExtraItem item, int qty) {
if (bookings.isEmpty() || orderBookingChoice == null || orderBookingChoice.getSelectedIndex() < 0) {
message("Please create a room booking first before adding extras.");
return;
}
Booking b = bookings.get(orderBookingChoice.getSelectedIndex());
double addedCost = item.price * qty;
b.extrasTotal += addedCost;
b.total += addedCost;
String entry = qty + "x " + item.name + " (R " + money(addedCost) + ")";
b.extrasSummary = (b.extrasSummary == null || b.extrasSummary.isEmpty())
? entry
: b.extrasSummary + "; " + entry;
saveBookings();
refreshBookingList();
updateOrderSummary();
message("Added " + entry + " to " + b.guest + ".\nUpdated Total: R " + money(b.total));
}
private void clearExtrasOnSelectedBooking() {
if (bookings.isEmpty() || orderBookingChoice == null || orderBookingChoice.getSelectedIndex() < 0) return;
Booking b = bookings.get(orderBookingChoice.getSelectedIndex());
if (b.extrasTotal <= 0) {
message("This booking has no extras to clear.");
return;
}
int confirm = JOptionPane.showConfirmDialog(this, "Remove all added food, drinks & amenities from booking " + b.id + "?", "Clear Extras", JOptionPane.YES_NO_OPTION);
if (confirm == JOptionPane.YES_OPTION) {
b.total -= b.extrasTotal;
b.extrasTotal = 0;
b.extrasSummary = "";
saveBookings();
refreshBookingList();
updateOrderSummary();
}
}
private void updateEstimate() {
if (roomChoice==null || totalLabel==null) return;
Room r=rooms.get(roomChoice.getSelectedIndex());
try {
LocalDate in=LocalDate.parse(checkInField.getText().trim());
LocalDate out=LocalDate.parse(checkOutField.getText().trim());
long nights=ChronoUnit.DAYS.between(in,out);
if (roomDescription!=null) roomDescription.setText(r.description);
totalLabel.setText(nights>0 ? "Estimated total: R "+money(nights*r.price) + " ("+nights+" nights)" : "Check your dates");
} catch(Exception ex) { totalLabel.setText("Enter valid dates (YYYY-MM-DD)"); }
}
private void processBooking() {
String enteredGuest = guestNameField.getText().trim();
if (enteredGuest.isEmpty()) {
message("Please enter the Guest Name before continuing.");
guestNameField.requestFocus();
return;
}
String enteredEmail = guestEmailField.getText().trim();
if (enteredEmail.isEmpty() || !enteredEmail.contains("@")) {
message("Please enter a valid Guest Email address before continuing.");
guestEmailField.requestFocus();
return;
}
try {
Room r=rooms.get(roomChoice.getSelectedIndex());
LocalDate in=LocalDate.parse(checkInField.getText().trim());
LocalDate out=LocalDate.parse(checkOutField.getText().trim());
int guests=(Integer)guestsSpinner.getValue();
long nights=ChronoUnit.DAYS.between(in,out);
if (!out.isAfter(in) || in.isBefore(LocalDate.now())) { message("Please choose valid future check-in and check-out dates."); return; }
if (guests>r.capacity) { message(r.name+" accommodates up to "+r.capacity+" guests."); return; }
double total=nights*r.price;
Object[] methods={"Card (demo)","Bank Transfer (demo)","Pay at Hotel"};
String method=(String)JOptionPane.showInputDialog(this,"Choose a payment method:","Payment",
JOptionPane.PLAIN_MESSAGE,null,methods,"Card (demo)");
if (method==null) return;
if (method.startsWith("Card")) {
JTextField card=new JTextField(); JTextField expiry=new JTextField();
JPanel pay=new JPanel(new GridLayout(0,1,5,5)); pay.add(new JLabel("Demo card number")); pay.add(card);
pay.add(new JLabel("Expiry (MM/YY)")); pay.add(expiry);
int result=JOptionPane.showConfirmDialog(this,pay,"Payment details (demo only)",JOptionPane.OK_CANCEL_OPTION);
if (result!=JOptionPane.OK_OPTION || card.getText().trim().isEmpty() || expiry.getText().trim().isEmpty()) {
message("Payment cancelled or incomplete."); return;
}
}
lastBooking = new Booking(
UUID.randomUUID().toString().substring(0,8).toUpperCase(),
enteredGuest + " (Admin: " + currentUser + ")",
enteredEmail,
r.name,
in.toString(),
out.toString(),
guests,
total,
method,
0.0,
""
);
bookings.add(lastBooking);
saveBookings();
refreshBookingList();
refreshOrderBookingDropdown();
guestNameField.setText("");
guestEmailField.setText("");
showConfirmation(lastBooking);
} catch(Exception ex) { message("Please enter dates in YYYY-MM-DD format."); }
}
private void showConfirmation(Booking b) {
JPanel panel=new JPanel(new GridBagLayout()); panel.setBackground(WINE);
JPanel details=new JPanel(); details.setBackground(WINE); details.setLayout(new BoxLayout(details,BoxLayout.Y_AXIS));
details.setBorder(new EmptyBorder(25,35,25,35));
JLabel check=new JLabel("✓",SwingConstants.CENTER); check.setForeground(GOLD); check.setFont(new Font("SansSerif",Font.BOLD,54)); check.setAlignmentX(.5f);
JLabel title=new JLabel("BOOKING CONFIRMED",SwingConstants.CENTER); title.setForeground(GOLD); title.setFont(new Font("Serif",Font.BOLD,25)); title.setAlignmentX(.5f);
JLabel thanks=new JLabel("Thank you for booking with The Imperial Solis",SwingConstants.CENTER); thanks.setForeground(CREAM); thanks.setAlignmentX(.5f);
JTextArea info=new JTextArea("Booking reference: "+b.id+"\nGuest: "+b.guest+"\nEmail: "+b.email+"\nRoom: "+b.room+"\nCheck-in: "+b.in+"\nCheck-out: "+b.out+"\nGuests: "+b.guests+"\nPayment: "+b.payment+"\nTotal: R "+money(b.total));
info.setEditable(false); info.setBackground(new Color(92,18,43)); info.setForeground(CREAM); info.setFont(new Font("Monospaced",Font.PLAIN,14)); info.setBorder(new EmptyBorder(18,18,18,18));
JButton home=goldButton("BACK TO HOME"); home.setAlignmentX(.5f); home.addActionListener(e->showDashboard());
details.add(check); details.add(title); details.add(Box.createVerticalStrut(8)); details.add(thanks); details.add(Box.createVerticalStrut(25)); details.add(info); details.add(Box.createVerticalStrut(20)); details.add(home);
panel.add(details); setContentPane(centered(panel)); revalidate(); repaint();
}
private JPanel bookingsPanel() {
JPanel p = new JPanel(new BorderLayout(10,10));
p.setBackground(CREAM);
p.setBorder(new EmptyBorder(22,25,22,25));
JPanel topBar = new JPanel(new BorderLayout(15, 0));
topBar.setBackground(CREAM);
topBar.add(sectionTitle("BOOKINGS"), BorderLayout.WEST);
JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
searchPanel.setBackground(CREAM);
searchPanel.add(label("Search (Name, Email or Ref #):"));
searchField = compactField("");
searchField.setToolTipText("Type guest name, email, or reference (e.g. BF16857E)");
searchField.getDocument().addDocumentListener(new DocumentListener() {
public void insertUpdate(DocumentEvent e) { refreshBookingList(); }
public void removeUpdate(DocumentEvent e) { refreshBookingList(); }
public void changedUpdate(DocumentEvent e) { refreshBookingList(); }
});
searchPanel.add(searchField);
JButton clearSearchBtn = goldButton("CLEAR");
clearSearchBtn.addActionListener(e -> {
searchField.setText("");
refreshBookingList();
});
searchPanel.add(clearSearchBtn);
topBar.add(searchPanel, BorderLayout.EAST);
p.add(topBar, BorderLayout.NORTH);
bookingList = new JList<>(bookingListModel);
bookingList.setFont(new Font("SansSerif",Font.PLAIN,14));
bookingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
refreshBookingList();
p.add(new JScrollPane(bookingList),BorderLayout.CENTER);
JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
actions.setBackground(CREAM);
JButton addExtrasShortcut = goldButton("ORDER FOOD / EXTRAS");
addExtrasShortcut.addActionListener(e -> {
int idx = bookingList.getSelectedIndex();
if (idx >= 0 && idx < displayedBookings.size() && orderBookingChoice != null) {
Booking sel = displayedBookings.get(idx);
int fullIdx = bookings.indexOf(sel);
if (fullIdx >= 0) orderBookingChoice.setSelectedIndex(fullIdx);
}
if (tabs != null) tabs.setSelectedIndex(4);
});
JButton addManualBtn = goldButton("ADD MANUAL BOOKING");
addManualBtn.addActionListener(e -> addManualBookingDialog());
JButton refresh = goldButton("REFRESH");
refresh.addActionListener(e -> {
loadBookings();
refreshBookingList();
refreshOrderBookingDropdown();
});
JButton cancelBtn = wineButton("CANCEL BOOKING");
cancelBtn.addActionListener(e -> cancelSelectedBooking());
actions.add(addExtrasShortcut);
actions.add(addManualBtn);
actions.add(refresh);
actions.add(cancelBtn);
p.add(actions, BorderLayout.SOUTH);
return p;
}
private void addManualBookingDialog() {
JTextField guestField = new JTextField();
JTextField emailField = new JTextField();
List roomOptions = new ArrayList<>();
for (Room r : rooms) roomOptions.add(r.name);
for (SpecialOffer so : specialOffers) roomOptions.add("Special Offer: " + so.title);
JComboBox roomBox = new JComboBox<>(new Vector<>(roomOptions));
JTextField inField = new JTextField(LocalDate.now().plusDays(1).toString());
JTextField outField = new JTextField(LocalDate.now().plusDays(3).toString());
JSpinner guestSpin = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
JTextField totalField = new JTextField("1640.00");
JComboBox payBox = new JComboBox<>(new Vector<>(Arrays.asList("Card", "Bank Transfer", "Pay at Hotel")));
JPanel form = new JPanel(new GridLayout(0, 2, 10, 8));
form.add(new JLabel("Guest Name:"));
form.add(guestField);
form.add(new JLabel("Guest Email:"));
form.add(emailField);
form.add(new JLabel("Room / Offer:"));
form.add(roomBox);
form.add(new JLabel("Check-in (YYYY-MM-DD):"));
form.add(inField);
form.add(new JLabel("Check-out (YYYY-MM-DD):"));
form.add(outField);
form.add(new JLabel("Number of Guests:"));
form.add(guestSpin);
form.add(new JLabel("Total Amount (R):"));
form.add(totalField);
form.add(new JLabel("Payment Method:"));
form.add(payBox);
int res = JOptionPane.showConfirmDialog(this, form, "Add Manual Booking", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
if (res == JOptionPane.OK_OPTION) {
String guest = guestField.getText().trim();
String email = emailField.getText().trim();
if (guest.isEmpty() || email.isEmpty()) {
message("Please enter both Guest Name and Guest Email.");
return;
}
try {
LocalDate in = LocalDate.parse(inField.getText().trim());
LocalDate out = LocalDate.parse(outField.getText().trim());
if (!out.isAfter(in)) {
message("Check-out date must be after check-in date.");
return;
}
double total = Double.parseDouble(totalField.getText().trim());
int guests = (Integer) guestSpin.getValue();
String selectedRoom = (String) roomBox.getSelectedItem();
String payment = (String) payBox.getSelectedItem();
String id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
Booking manual = new Booking(id, guest + " (Admin: " + currentUser + ")", email, selectedRoom, in.toString(), out.toString(), guests, total, payment, 0.0, "");
bookings.add(manual);
saveBookings();
refreshBookingList();
refreshOrderBookingDropdown();
message("Manual booking " + id + " added for " + guest + ".");
} catch (Exception ex) {
message("Invalid date or total amount. Use YYYY-MM-DD for dates and numbers for total.");
}
}
}
private void cancelSelectedBooking() {
int idx = bookingList.getSelectedIndex();
if (idx < 0 || displayedBookings.isEmpty()) {
message("Please select a booking from the list to cancel.");
return;
}
Booking selected = displayedBookings.get(idx);
int confirm = JOptionPane.showConfirmDialog(
this,
"Are you sure you want to cancel booking " + selected.id + " (" + selected.room + ")?",
"Cancel Booking",
JOptionPane.YES_NO_OPTION,
JOptionPane.WARNING_MESSAGE
);
if (confirm == JOptionPane.YES_OPTION) {
bookings.remove(selected);
saveBookings();
refreshBookingList();
refreshOrderBookingDropdown();
message("Booking " + selected.id + " has been cancelled.");
}
}
private JPanel notificationsPanel() {
JPanel p=new JPanel(); p.setBackground(CREAM); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS)); p.setBorder(new EmptyBorder(25,35,25,35));
p.add(sectionTitle("NOTIFICATIONS")); p.add(Box.createVerticalStrut(20));
p.add(notification("Booking updates","All bookings and ordered extras remain saved across every admin login."));
p.add(notification("Guest orders","Website dining, drinks, Kids' Club & amenities can be added in Extras & Orders."));
p.add(notification("Check-in reminder","Please have your booking reference ready when you arrive."));
p.add(notification("Guest support","Contact the front desk for assistance during your stay."));
return p;
}
private JPanel notification(String title,String body) {
JPanel n=new JPanel(new BorderLayout(10,5)); n.setBackground(Color.WHITE); n.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true),new EmptyBorder(13,16,13,16)));
JLabel t=new JLabel("✦  "+title); t.setForeground(WINE); t.setFont(new Font("SansSerif",Font.BOLD,15));
JLabel b=new JLabel(body); b.setForeground(INK); n.add(t,BorderLayout.NORTH); n.add(b,BorderLayout.CENTER);
n.setMaximumSize(new Dimension(700,75)); return n;
}
private void refreshBookingList() {
bookingListModel.clear();
displayedBookings.clear();
String query = (searchField != null) ? searchField.getText().trim().toLowerCase() : "";
for (Booking b : bookings) {
boolean matches = query.isEmpty()
|| b.id.toLowerCase().contains(query)
|| b.guest.toLowerCase().contains(query)
|| b.email.toLowerCase().contains(query);
if (matches) {
displayedBookings.add(b);
String emailTag = (b.email != null && !b.email.isEmpty()) ? "  |  Email: " + b.email : "";
String extrasTag = (b.extrasSummary != null && !b.extrasSummary.isEmpty())
? "  |  Extras: [" + b.extrasSummary + "]"
: "";
bookingListModel.addElement(
b.id + "  |  Guest: " + b.guest + emailTag + "  |  " + b.room + "  |  " + b.in + " to " + b.out + extrasTag + "  |  Total: R " + money(b.total)
);
}
}
if (bookingListModel.isEmpty()) {
if (!query.isEmpty()) {
bookingListModel.addElement("No bookings found matching '" + searchField.getText().trim() + "'.");
} else {
bookingListModel.addElement("No bookings yet. Choose Book a Room or Add Manual Booking.");
}
}
}
private void loadBookings() {
bookings.clear();
try {
Files.createDirectories(DATA);
Path f = DATA.resolve("bookings.csv");
if (!Files.exists(f)) return;
for (String line : Files.readAllLines(f)) {
List<String> a = Arrays.asList(line.split(Pattern.quote("|"), -1));
if (a.size() == 11) {
bookings.add(new Booking(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5),
Integer.parseInt(a.get(6)), Double.parseDouble(a.get(7)), a.get(8),
Double.parseDouble(a.get(9)), a.get(10)));
} else if (a.size() == 10) {
bookings.add(new Booking(a.get(0), a.get(1), "", a.get(2), a.get(3), a.get(4),
Integer.parseInt(a.get(5)), Double.parseDouble(a.get(6)), a.get(7),
Double.parseDouble(a.get(8)), a.get(9)));
} else if (a.size() == 8) {
bookings.add(new Booking(a.get(0), a.get(1), "", a.get(2), a.get(3), a.get(4),
Integer.parseInt(a.get(5)), Double.parseDouble(a.get(6)), a.get(7),
0.0, ""));
}
}
} catch(Exception ignored) {}
}
private void saveBookings() {
try {
Files.createDirectories(DATA);
List lines = new ArrayList<>();
for (Booking b : bookings) {
lines.add(String.join("|", b.id, b.guest, b.email == null ? "" : b.email, b.room, b.in, b.out,
String.valueOf(b.guests), String.valueOf(b.total), b.payment,
String.valueOf(b.extrasTotal), b.extrasSummary == null ? "" : b.extrasSummary));
}
Files.write(DATA.resolve("bookings.csv"), lines);
} catch(IOException ex) { message("Booking saved for this session, but local file could not be updated."); }
}
private JPanel centered(JComponent c) { JPanel p=new JPanel(new GridBagLayout()); p.setBackground(CREAM); p.add(c); return p; }
private JLabel sectionTitle(String s) { JLabel l=new JLabel(s); l.setForeground(WINE); l.setFont(new Font("Serif",Font.BOLD,25)); return l; }
private JLabel label(String s) { JLabel l=new JLabel(s); l.setForeground(INK); l.setFont(new Font("SansSerif",Font.BOLD,13)); return l; }
private JTextField field(String hint) { JTextField f=new JTextField(22); f.setText(hint); styleField(f); return f; }
private JTextField compactField(String hint) { JTextField f=new JTextField(16); f.setText(hint); styleField(f); return f; }
private void styleField(JTextField f) { f.setFont(new Font("SansSerif",Font.PLAIN,14)); f.setBackground(CREAM); f.setForeground(INK); f.setBorder(new CompoundBorder(new LineBorder(new Color(210,198,170),1,true),new EmptyBorder(7,10,7,10))); }
private void styleCombo(JComboBox<?> c) { c.setFont(new Font("SansSerif",Font.PLAIN,14)); c.setBackground(CREAM); }
private JButton wineButton(String s) {
JButton b = new JButton(s);
b.setBackground(WINE);
b.setForeground(Color.WHITE);
b.setOpaque(true);
b.setContentAreaFilled(true);
b.setBorderPainted(false);
b.setFocusPainted(false);
b.setFont(new Font("SansSerif", Font.BOLD, 13));
b.setBorder(new EmptyBorder(11, 20, 11, 20));
return b;
}
private JButton goldButton(String s) {
JButton b = new JButton(s);
b.setBackground(GOLD);
b.setForeground(WINE);
b.setOpaque(true);
b.setContentAreaFilled(true);
b.setBorderPainted(false);
b.setFocusPainted(false);
b.setFont(new Font("SansSerif", Font.BOLD, 13));
b.setBorder(new EmptyBorder(11, 20, 11, 20));
return b;
}
private void message(String s) { JOptionPane.showMessageDialog(this,s,"Imperial Solis",JOptionPane.INFORMATION_MESSAGE); }
private String money(double n) { return String.format("%,.2f",n); }
private static class Room {
String name,description; double price; int capacity;
Room(String n,double p,int c,String d){name=n;price=p;capacity=c;description=d;}
}
private static class SpecialOffer {
String title, badge, priceLabel, description;
int defaultRoomIndex;
SpecialOffer(String title, String badge, String priceLabel, String description, int defaultRoomIndex) {
this.title = title;
this.badge = badge;
this.priceLabel = priceLabel;
this.description = description;
this.defaultRoomIndex = defaultRoomIndex;
}
}
private static class ExtraItem {
String category, name, description;
double price;
ExtraItem(String category, String name, double price, String description) {
this.category = category;
this.name = name;
this.price = price;
this.description = description;
}
}
private static class Booking {
String id,guest,email,room,in,out,payment,extrasSummary; int guests; double total,extrasTotal;
Booking(String id,String guest,String email,String room,String in,String out,int guests,double total,String payment,double extrasTotal,String extrasSummary){
this.id=id;this.guest=guest;this.email=email;this.room=room;this.in=in;this.out=out;this.guests=guests;this.total=total;this.payment=payment;
this.extrasTotal=extrasTotal;this.extrasSummary=extrasSummary;
}
}
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
try { UIManager.setLookAndFeel(UIManager.
getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
new try1().setVisible(true);
});
}
}
