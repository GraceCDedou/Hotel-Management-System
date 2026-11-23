package com.imperialsolis;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class ImperialSolisHotel extends JFrame {
    private static final Color WINE = new Color(76, 8, 31);
    private static final Color GOLD = new Color(205, 169, 91);
    private static final Color CREAM = new Color(250, 247, 235);
    private static final Color INK = new Color(55, 42, 35);
    private static final Path DATA = Paths.get(System.getProperty("user.home"), ".imperial-solis");
    private final List<Room> rooms = List.of(
        new Room("Standard Room", 1700, 2, "Comfortable room with Wi-Fi, air conditioning and city view."),
        new Room("Deluxe Room", 2500, 2, "Elegant room with king bed, balcony and premium amenities."),
        new Room("Suite", 3600, 4, "Spacious luxury suite with lounge area and panoramic views.")
    );
    private final List<Booking> bookings = new ArrayList<>();
    private String currentUser = "Guest";
    private JLabel welcomeLabel;
    private DefaultListModel<String> bookingListModel = new DefaultListModel<>();
    private JComboBox<String> roomChoice;
    private JSpinner guestsSpinner;
    private JTextField checkInField, checkOutField;
    private JTextArea roomDescription;
    private JLabel totalLabel;
    private Booking lastBooking;

    public ImperialSolisHotel() {
        super("The Imperial Solis | Hotel & Resort");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 620));
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
        JTextField username = field("Username");
        JPasswordField password = new JPasswordField(); styleField(password); password.setToolTipText("Password");
        JButton login = wineButton("LOGIN");
        JButton register = goldButton("CREATE ACCOUNT");
        g.gridy=0; panel.add(crest,g); g.gridy++; panel.add(title,g);
        g.gridy++; panel.add(username,g); g.gridy++; panel.add(password,g);
        g.gridy++; panel.add(login,g); g.gridy++; panel.add(register,g);
        login.addActionListener(e -> {
            String u = username.getText().trim();
            if (u.isEmpty() || password.getPassword().length == 0) {
                message("Enter a username and password to continue."); return;
            }
            currentUser = u; showDashboard();
        });
        register.addActionListener(e -> {
            String u = username.getText().trim();
            if (u.isEmpty() || password.getPassword().length == 0) {
                message("Choose a username and password first."); return;
            }
            try {
                Files.createDirectories(DATA);
                Files.writeString(DATA.resolve("users.txt"), u + "," + new String(password.getPassword()) + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                currentUser = u;
                message("Account created. Welcome to Imperial Solis!");
                showDashboard();
            } catch (IOException ex) { message("Could not save account: " + ex.getMessage()); }
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

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Home", homePanel(tabs));
        tabs.addTab("Rooms", roomsPanel());
        tabs.addTab("Book a Room", bookingPanel());
        tabs.addTab("My Bookings", myBookingsPanel());
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
        String[] labels = {"ROOMS", "MY BOOKINGS", "SPECIAL OFFERS", "GUEST SUPPORT"};
        String[] desc = {"Explore our luxury rooms", "View your reservations", "Discover seasonal packages", "We are here to assist you"};
        for (int i=0;i<labels.length;i++) {
            JPanel card = new JPanel(new BorderLayout(5,8)); card.setBackground(Color.WHITE);
            card.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true), new EmptyBorder(20,20,20,20)));
            JLabel l = new JLabel(labels[i], SwingConstants.CENTER); l.setForeground(WINE);
            l.setFont(new Font("SansSerif",Font.BOLD,17));
            JLabel d = new JLabel(desc[i], SwingConstants.CENTER); d.setForeground(INK);
            card.add(l,BorderLayout.CENTER); card.add(d,BorderLayout.SOUTH); cards.add(card);
        }
        content.add(cards);
        JButton book = goldButton("BOOK YOUR STAY");
        book.addActionListener(e -> tabs.setSelectedIndex(2));
        content.add(Box.createVerticalStrut(30)); content.add(book);
        p.add(content); return p;
    }

    private JPanel roomsPanel() {
        JPanel p = new JPanel(new BorderLayout(15,15)); p.setBackground(CREAM);
        p.setBorder(new EmptyBorder(22,25,22,25));
        JLabel title = sectionTitle("ROOM LISTINGS");
        p.add(title,BorderLayout.NORTH);
        JPanel list = new JPanel(new GridLayout(0,1,12,12)); list.setBackground(CREAM);
        for (Room r: rooms) {
            JPanel card = new JPanel(new BorderLayout(15,8)); card.setBackground(Color.WHITE);
            card.setBorder(new CompoundBorder(new LineBorder(new Color(220,210,185),1,true),new EmptyBorder(17,20,17,20)));
            JLabel name = new JLabel(r.name); name.setFont(new Font("Serif",Font.BOLD,21)); name.setForeground(WINE);
            JLabel price = new JLabel("R " + money(r.price) + " / night  •  Up to " + r.capacity + " guests");
            price.setFont(new Font("SansSerif",Font.BOLD,14)); price.setForeground(INK);
            JLabel desc = new JLabel(r.description);
            JPanel text = new JPanel(new GridLayout(3,1)); text.setOpaque(false);
            text.add(name); text.add(price); text.add(desc);
            JButton select = goldButton("BOOK NOW");
            select.addActionListener(e -> { showDashboard(); /* booking tab is selected below */ });
            card.add(text,BorderLayout.CENTER); card.add(select,BorderLayout.EAST); list.add(card);
        }
        p.add(list,BorderLayout.CENTER);
        return p;
    }

    private JPanel bookingPanel() {
        JPanel p = new JPanel(new GridBagLayout()); p.setBackground(CREAM);
        JPanel form = new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE);
        form.setBorder(new CompoundBorder(new LineBorder(GOLD,1,true),new EmptyBorder(24,30,24,30)));
        GridBagConstraints g = new GridBagConstraints(); g.insets=new Insets(8,8,8,8); g.fill=GridBagConstraints.HORIZONTAL; g.gridx=0; g.weightx=1;
        JLabel title=sectionTitle("BOOKING FLOW");
        g.gridy=0; form.add(title,g);
        g.gridy++; form.add(label("Room Type"),g);
        roomChoice = new JComboBox<>(rooms.stream().map(r->r.name).toArray(String[]::new));
        styleCombo(roomChoice); g.gridy++; form.add(roomChoice,g);
        g.gridy++; form.add(label("Check-in date (YYYY-MM-DD)"),g);
        checkInField=field(LocalDate.now().plusDays(1).toString()); g.gridy++; form.add(checkInField,g);
        g.gridy++; form.add(label("Check-out date (YYYY-MM-DD)"),g);
        checkOutField=field(LocalDate.now().plusDays(3).toString()); g.gridy++; form.add(checkOutField,g);
        g.gridy++; form.add(label("Number of guests"),g);
        guestsSpinner=new JSpinner(new SpinnerNumberModel(2,1,8,1)); g.gridy++; form.add(guestsSpinner,g);
        roomDescription=new JTextArea(3,28); roomDescription.setLineWrap(true); roomDescription.setWrapStyleWord(true);
        roomDescription.setEditable(false); roomDescription.setBackground(CREAM); roomDescription.setText(rooms.get(0).description);
        g.gridy++; form.add(roomDescription,g);
        totalLabel=new JLabel("Estimated total: R 0.00"); totalLabel.setForeground(WINE); totalLabel.setFont(new Font("SansSerif",Font.BOLD,16));
        g.gridy++; form.add(totalLabel,g);
        JButton pay=wineButton("CONTINUE TO PAYMENT");
        g.gridy++; form.add(pay,g);
        roomChoice.addActionListener(e -> updateEstimate());
        checkInField.addActionListener(e -> updateEstimate()); checkOutField.addActionListener(e -> updateEstimate());
        guestsSpinner.addChangeListener(e -> updateEstimate());
        pay.addActionListener(e -> processBooking());
        updateEstimate(); p.add(form); return p;
    }

    private void updateEstimate() {
        if (roomChoice==null || totalLabel==null) return;
        Room r=rooms.get(roomChoice.getSelectedIndex());
        try {
            LocalDate in=LocalDate.parse(checkInField.getText().trim());
            LocalDate out=LocalDate.parse(checkOutField.getText().trim());
            long nights=ChronoUnit.DAYS.between(in,out);
            if (roomDescription!=null) roomDescription.setText(r.description);
            totalLabel.setText("Estimated total: " + (nights>0 ? "R "+money(nights*r.price) + " ("+nights+" nights)" : "Check your dates"));
        } catch(Exception ex) { totalLabel.setText("Estimated total: Enter valid dates (YYYY-MM-DD)"); }
    }

    private void processBooking() {
        try {
            Room r=rooms.get(roomChoice.getSelectedIndex());
            LocalDate in=LocalDate.parse(checkInField.getText().trim());
            LocalDate out=LocalDate.parse(checkOutField.getText().trim());
            int guests=(Integer)guestsSpinner.getValue();
            long nights=ChronoUnit.DAYS.between(in,out);
            if (!out.isAfter(in) || in.isBefore(LocalDate.now())) { message("Please choose valid future check-in and check-out dates."); return; }
            if (guests>r.capacity) { message(r.name+" accommodates up to "+r.capacity+" guests."); return; }
            double total=nights*r.price;
            String[] methods={"Card (demo)","Bank Transfer (demo)","Pay at Hotel"};
            String method=(String)JOptionPane.showInputDialog(this,"Choose a payment method:","Payment",
                JOptionPane.PLAIN_MESSAGE,null,methods,methods[0]);
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
            lastBooking=new Booking(UUID.randomUUID().toString().substring(0,8).toUpperCase(),currentUser,r.name,in.toString(),out.toString(),guests,total,method);
            bookings.add(lastBooking); saveBookings(); showConfirmation(lastBooking);
        } catch(Exception ex) { message("Please enter dates in YYYY-MM-DD format."); }
    }

    private void showConfirmation(Booking b) {
        JPanel panel=new JPanel(new GridBagLayout()); panel.setBackground(WINE);
        JPanel details=new JPanel(); details.setBackground(WINE); details.setLayout(new BoxLayout(details,BoxLayout.Y_AXIS));
        details.setBorder(new EmptyBorder(25,35,25,35));
        JLabel check=new JLabel("✓",SwingConstants.CENTER); check.setForeground(GOLD); check.setFont(new Font("SansSerif",Font.BOLD,54)); check.setAlignmentX(.5f);
        JLabel title=new JLabel("BOOKING CONFIRMED",SwingConstants.CENTER); title.setForeground(GOLD); title.setFont(new Font("Serif",Font.BOLD,25)); title.setAlignmentX(.5f);
        JLabel thanks=new JLabel("Thank you for booking with The Imperial Solis",SwingConstants.CENTER); thanks.setForeground(CREAM); thanks.setAlignmentX(.5f);
        JTextArea info=new JTextArea("Booking reference: "+b.id+"\nGuest: "+b.guest+"\nRoom: "+b.room+"\nCheck-in: "+b.in+"\nCheck-out: "+b.out+"\nGuests: "+b.guests+"\nPayment: "+b.payment+"\nTotal: R "+money(b.total));
        info.setEditable(false); info.setBackground(new Color(92,18,43)); info.setForeground(CREAM); info.setFont(new Font("Monospaced",Font.PLAIN,14)); info.setBorder(new EmptyBorder(18,18,18,18));
        JButton home=goldButton("BACK TO HOME"); home.setAlignmentX(.5f); home.addActionListener(e->showDashboard());
        details.add(check); details.add(title); details.add(Box.createVerticalStrut(8)); details.add(thanks); details.add(Box.createVerticalStrut(25)); details.add(info); details.add(Box.createVerticalStrut(20)); details.add(home);
        panel.add(details); setContentPane(centered(panel)); revalidate(); repaint();
    }

    private JPanel myBookingsPanel() {
        JPanel p=new JPanel(new BorderLayout(10,10)); p.setBackground(CREAM); p.setBorder(new EmptyBorder(22,25,22,25));
        p.add(sectionTitle("MY BOOKINGS"),BorderLayout.NORTH);
        JList<String> list=new JList<>(bookingListModel); list.setFont(new Font("SansSerif",Font.PLAIN,14));
        refreshBookingList(); p.add(new JScrollPane(list),BorderLayout.CENTER);
        JButton refresh=goldButton("REFRESH"); refresh.addActionListener(e->refreshBookingList()); p.add(refresh,BorderLayout.SOUTH);
        return p;
    }

    private JPanel notificationsPanel() {
        JPanel p=new JPanel(); p.setBackground(CREAM); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS)); p.setBorder(new EmptyBorder(25,35,25,35));
        p.add(sectionTitle("NOTIFICATIONS")); p.add(Box.createVerticalStrut(20));
        p.add(notification("Booking updates","Your reservation details are saved in My Bookings."));
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
        for (Booking b:bookings) if (b.guest.equals(currentUser))
            bookingListModel.addElement(b.id+"  |  "+b.room+"  |  "+b.in+" to "+b.out+"  |  R "+money(b.total));
        if (bookingListModel.isEmpty()) bookingListModel.addElement("No bookings yet. Choose Book a Room to make a reservation.");
    }

    private void loadBookings() {
        try {
            Files.createDirectories(DATA); Path f=DATA.resolve("bookings.csv");
            if (!Files.exists(f)) return;
            for (String line:Files.readAllLines(f)) {
                String[] a=line.split("\\|",-1); if(a.length==8)
                    bookings.add(new Booking(a[0],a[1],a[2],a[3],a[4],Integer.parseInt(a[5]),Double.parseDouble(a[6]),a[7]));
            }
        } catch(Exception ignored) {}
    }

    private void saveBookings() {
        try {
            Files.createDirectories(DATA);
            List<String> lines=new ArrayList<>();
            for(Booking b:bookings) lines.add(String.join("|",b.id,b.guest,b.room,b.in,b.out,
                String.valueOf(b.guests),String.valueOf(b.total),b.payment));
            Files.write(DATA.resolve("bookings.csv"),lines);
        } catch(IOException ex) { message("Booking saved for this session, but local file could not be updated."); }
    }

    private JPanel centered(JComponent c) { JPanel p=new JPanel(new GridBagLayout()); p.setBackground(CREAM); p.add(c); return p; }
    private JLabel sectionTitle(String s) { JLabel l=new JLabel(s); l.setForeground(WINE); l.setFont(new Font("Serif",Font.BOLD,25)); return l; }
    private JLabel label(String s) { JLabel l=new JLabel(s); l.setForeground(INK); l.setFont(new Font("SansSerif",Font.BOLD,13)); return l; }
    private JTextField field(String hint) { JTextField f=new JTextField(22); f.setText(hint); styleField(f); return f; }
    private void styleField(JTextField f) { f.setFont(new Font("SansSerif",Font.PLAIN,14)); f.setBackground(CREAM); f.setForeground(INK); f.setBorder(new CompoundBorder(new LineBorder(new Color(210,198,170),1,true),new EmptyBorder(9,11,9,11))); }
    private void styleCombo(JComboBox<?> c) { c.setFont(new Font("SansSerif",Font.PLAIN,14)); c.setBackground(CREAM); }
    private JButton wineButton(String s) { JButton b=new JButton(s); b.setBackground(WINE); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("SansSerif",Font.BOLD,13)); b.setBorder(new EmptyBorder(11,20,11,20)); return b; }
    private JButton goldButton(String s) { JButton b=new JButton(s); b.setBackground(GOLD); b.setForeground(WINE); b.setFocusPainted(false); b.setFont(new Font("SansSerif",Font.BOLD,13)); b.setBorder(new EmptyBorder(11,20,11,20)); return b; }
    private void message(String s) { JOptionPane.showMessageDialog(this,s,"Imperial Solis",JOptionPane.INFORMATION_MESSAGE); }
    private String money(double n) { return String.format("%,.2f",n); }

    private static class Room {
        String name,description; double price; int capacity;
        Room(String n,double p,int c,String d){name=n;price=p;capacity=c;description=d;}
    }
    private static class Booking {
        String id,guest,room,in,out,payment; int guests; double total;
        Booking(String id,String guest,String room,String in,String out,int guests,double total,String payment){
            this.id=id;this.guest=guest;this.room=room;this.in=in;this.out=out;this.guests=guests;this.total=total;this.payment=payment;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
            new ImperialSolisHotel().setVisible(true);
        });
    }
}
