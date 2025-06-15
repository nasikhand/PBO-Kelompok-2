package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.DestinasiController; // Diperlukan untuk mencari DestinasiModel berdasarkan nama
import model.DestinasiModel; // Diperlukan untuk mendapatkan ID Destinasi
import model.CustomTripDetailModel; // Diperlukan untuk detail itinerary
import com.toedter.calendar.JDateChooser; // Untuk date picker di tabel
import com.toedter.calendar.JTextFieldDateEditor; // Untuk styling JDateChooser editor

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent; // For JTextFieldDateEditor styling

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter; // <--- PASTIKAN IMPORT INI ADA
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class PanelItineraryStep extends JPanel {

    private MainAppFrame mainAppFrame;

    private JPanel panelBuildSteps;
    private JPanel panelCustomTripMain;
    private JSplitPane splitPaneContent;
    private JPanel panelLeftContent;
    private JPanel panelRightContent;

    private JLabel lblBuildStepsTitle;
    private JLabel lblStep1Destinasi;
    private JLabel lblStep2Itinerary; // Updated label
    private JLabel lblStep3TransportCost; // Updated label
    private JLabel lblStep4Participants; // Updated label
    private JLabel lblStep5Final;     // Updated label

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JPanel panelItineraryDetails;
    private JTable itineraryTable;
    private DefaultTableModel itineraryTableModel;
    private JScrollPane scrollItineraryTable; // <--- DEKLARASI INI DITAMBAHKAN
    private JButton btnMoveUp;
    private JButton btnMoveDown;

    private JPanel panelTripSummary;
    private JLabel lblSummaryDestinationsDisplay;
    private JLabel lblSummaryDatesDisplay;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasiSummaryDisplay;
    private final List<String> currentDestinations; // From DestinationStep
    private double initialEstimatedCost; // From DestinationStep

    private DestinasiController destinasiController;

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;


    public PanelItineraryStep(MainAppFrame mainAppFrame, List<String> destinations, double initialEstimatedCost) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.initialEstimatedCost = initialEstimatedCost;
        this.listModelDestinasiSummaryDisplay = new DefaultListModel<>();
        this.destinasiController = new DestinasiController(); // Inisialisasi DestinasiController

        initializeUI();
        applyAppTheme();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelBuildSteps = new JPanel();
        panelBuildSteps.setLayout(new BoxLayout(panelBuildSteps, BoxLayout.Y_AXIS));
        panelBuildSteps.setPreferredSize(new Dimension(230, 0));

        lblBuildStepsTitle = new JLabel(" Langkah Pembangunan");
        lblBuildStepsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBuildSteps.add(lblBuildStepsTitle);

        EmptyBorder stepLabelBorder = new EmptyBorder(8, 15, 8, 10);

        lblStep1Destinasi = new JLabel(); lblStep1Destinasi.setBorder(stepLabelBorder); lblStep1Destinasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep2Itinerary = new JLabel(); lblStep2Itinerary.setBorder(stepLabelBorder); lblStep2Itinerary.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep3TransportCost = new JLabel(); lblStep3TransportCost.setBorder(stepLabelBorder); lblStep3TransportCost.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep4Participants = new JLabel(); lblStep4Participants.setBorder(stepLabelBorder); lblStep4Participants.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep5Final = new JLabel();     lblStep5Final.setBorder(stepLabelBorder);     lblStep5Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelBuildSteps.add(lblStep1Destinasi);
        panelBuildSteps.add(lblStep2Itinerary);
        panelBuildSteps.add(lblStep3TransportCost);
        panelBuildSteps.add(lblStep4Participants);
        panelBuildSteps.add(lblStep5Final);
        panelBuildSteps.add(Box.createVerticalGlue());

        add(panelBuildSteps, BorderLayout.WEST);

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        panelCustomTripMain.setBorder(new EmptyBorder(0, 10, 0, 0));

        this.panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Atur Itinerary");
        btnSaveTrip = new JButton("Simpan Draf Trip");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5));

        // --- Itinerary Details (JTable) ---
        panelItineraryDetails = new JPanel(new BorderLayout());
        String[] columnNames = {"Urutan", "Destinasi", "Tanggal Kunjungan", "Durasi (Jam)"};
        itineraryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3; // Tanggal Kunjungan dan Durasi bisa diedit
            }
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Date.class; // Tanggal Kunjungan
                if (column == 3) return Integer.class; // Durasi (untuk input number)
                return String.class; // Urutan, Destinasi
            }
        };
        itineraryTable = new JTable(itineraryTableModel);
        itineraryTable.setRowHeight(28); // Set row height for JDateChooser

        // Set custom TableCellEditor for Date column
        itineraryTable.getColumnModel().getColumn(2).setCellEditor(new DateChooserEditor());
        itineraryTable.getColumnModel().getColumn(2).setCellRenderer(new DateChooserRenderer());
        
        // Populate table with initial destinations
        for (int i = 0; i < currentDestinations.size(); i++) {
            itineraryTableModel.addRow(new Object[]{i + 1, currentDestinations.get(i), null, 0}); // Default null date, 0 duration
        }

        scrollItineraryTable = new JScrollPane(itineraryTable); // <--- INISIALISASI DI SINI
        panelItineraryDetails.add(scrollItineraryTable, BorderLayout.CENTER);

        // Move Up/Down Buttons
        JPanel moveButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnMoveUp = new JButton("▲ Pindah Atas");
        btnMoveDown = new JButton("▼ Pindah Bawah");
        moveButtonsPanel.add(btnMoveUp);
        moveButtonsPanel.add(btnMoveDown);
        panelItineraryDetails.add(moveButtonsPanel, BorderLayout.SOUTH);

        panelLeftContent.add(panelItineraryDetails);
        panelLeftContent.add(Box.createVerticalGlue());

        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0));

        // --- Trip Summary Section (Right Side) ---
        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        lblSummaryDestinationsDisplay = new JLabel("Destinasi Terpilih:");
        listModelDestinasiSummaryDisplay = new DefaultListModel<>();
        listDestinasiSummary = new JList<>(listModelDestinasiSummaryDisplay);
        listDestinasiSummary.setEnabled(false);
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        jScrollPaneDestinasiSummary.setPreferredSize(new Dimension(0, 100)); // Fixed height

        JPanel summaryContentTop = new JPanel();
        summaryContentTop.setLayout(new BoxLayout(summaryContentTop, BoxLayout.Y_AXIS));
        summaryContentTop.setOpaque(false);
        summaryContentTop.add(lblSummaryDestinationsDisplay);
        summaryContentTop.add(jScrollPaneDestinasiSummary);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));
        
        lblSummaryDatesDisplay = new JLabel("Tanggal Trip Keseluruhan: -");
        summaryContentTop.add(lblSummaryDatesDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));

        panelTripSummary.add(summaryContentTop, BorderLayout.NORTH);

        panelRightContent.add(panelTripSummary);
        panelRightContent.add(Box.createRigidArea(new Dimension(0,10)));

        // --- Estimated Cost Section (Right Bottom) ---
        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryTitleInfo = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60));
        panelRightContent.add(panelEstimatedCost);
        panelRightContent.add(Box.createVerticalGlue());

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(420);
        splitPaneContent.setResizeWeight(0.48);
        splitPaneContent.setContinuousLayout(true);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        this.panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Destinasi");
        btnNextStep = new JButton("Lanjut ke Biaya Transport >");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnNextStep, BorderLayout.EAST);
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0));
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        this.add(panelMainHeader, BorderLayout.NORTH);
        this.add(splitPaneContent, BorderLayout.CENTER);
        this.add(panelMainFooter, BorderLayout.SOUTH);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        panelBuildSteps.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        panelBuildSteps.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR),
            new EmptyBorder(10, 0, 10, 0)
        ));
        lblBuildStepsTitle.setFont(AppTheme.FONT_SUBTITLE);
        lblBuildStepsTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        lblBuildStepsTitle.setBorder(new EmptyBorder(10, 10, 15, 10));

        panelCustomTripMain.setBackground(Color.WHITE);
        panelCustomTripMain.setBorder(new EmptyBorder(15,20,15,20));

        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnSaveTrip, "Simpan Draf Trip");

        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;

        panelItineraryDetails.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Atur Urutan & Tanggal Kunjungan",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        panelItineraryDetails.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);

        itineraryTable.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        itineraryTable.setBackground(AppTheme.INPUT_BACKGROUND);
        itineraryTable.setForeground(AppTheme.INPUT_TEXT);
        itineraryTable.getTableHeader().setFont(AppTheme.FONT_PRIMARY_MEDIUM_BOLD);
        itineraryTable.getTableHeader().setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        itineraryTable.getTableHeader().setForeground(AppTheme.TEXT_DARK);
        scrollItineraryTable.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR)); // Style the scroll pane border

        stylePrimaryButton(btnMoveUp, "▲");
        stylePrimaryButton(btnMoveDown, "▼");

        lblSummaryDestinationsDisplay.setFont(AppTheme.FONT_LABEL_FORM); // Header for destinations list
        lblSummaryDestinationsDisplay.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(new Color(0,0,0,0)); // Transparent
        listDestinasiSummary.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createEmptyBorder()); // No border
        jScrollPaneDestinasiSummary.getViewport().setOpaque(false);

        lblSummaryDatesDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryDatesDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        

        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Destinasi");
        stylePrimaryButton(btnNextStep, "Lanjut ke Biaya Transport >");
    }

    private void stylePrimaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }

    private void styleSecondaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
    }
    
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        if (button == null) return;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void setupLogicAndVisuals() {
        updateBuildStepLabels(2); // Set active step for Itinerary

        updateSummaryDisplays();
        updateEstimatedCost();

        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);

        if (btnSaveTrip != null) {
            btnSaveTrip.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                    this, 
                    "Harap selesaikan semua langkah hingga tahap finalisasi untuk dapat menyimpan draf.", 
                    "Informasi", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            });
        }

        btnMoveUp.addActionListener(e -> moveTableRow(-1));
        btnMoveDown.addActionListener(e -> moveTableRow(1));

        itineraryTableModel.addTableModelListener(e -> {
            updateSummaryDisplays();
            updateEstimatedCost();
            updateNextStepButtonState();
        });
        
        itineraryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateMoveButtonsState();
            }
        });

        updateNextStepButtonState();
        updateMoveButtonsState();
    }

    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {
            lblStep1Destinasi,
            lblStep2Itinerary,
            lblStep3TransportCost,
            lblStep4Participants,
            lblStep5Final
        };
        String[] stepTexts = {
            "1. Destinasi",
            "2. Itinerary",
            "3. Biaya Transport",
            "4. Peserta",
            "5. Finalisasi"
        };

        for (int i = 0; i < 5; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }

    private void populateSummaryDisplay() {
        listModelDestinasiSummaryDisplay.clear(); // Clear existing items
        LocalDate overallStartDate = null;
        LocalDate overallEndDate = null;

        // DateTimeFormatter is needed for formatting LocalDate objects
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMMyyyy"); 

        for (int i = 0; i < itineraryTableModel.getRowCount(); i++) {
            String destinasiName = (String) itineraryTableModel.getValueAt(i, 1);
            Object visitDateObj = itineraryTableModel.getValueAt(i, 2); // Get Object as it could be Date or null
            LocalDate visitDate = null;

            // Convert to LocalDate for consistency and calculations
            if (visitDateObj instanceof Date) {
                visitDate = ((Date) visitDateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (visitDateObj instanceof LocalDate) {
                visitDate = (LocalDate) visitDateObj;
            }

            String dateString = (visitDate != null) ? visitDate.format(dtf) : "Belum diatur";

            listModelDestinasiSummaryDisplay.addElement((i + 1) + ". " + destinasiName + " (" + dateString + ")");

            // Track overall start and end dates for the entire trip
            if (visitDate != null) {
                if (overallStartDate == null || visitDate.isBefore(overallStartDate)) {
                    overallStartDate = visitDate;
                }
                if (overallEndDate == null || visitDate.isAfter(overallEndDate)) {
                    overallEndDate = visitDate;
                }
            }
        }
        
        // Update overall trip dates display
        String startDateFormatted = (overallStartDate != null) ? overallStartDate.format(dtf) : "-";
        String endDateFormatted = (overallEndDate != null) ? overallEndDate.format(dtf) : "-";
        lblSummaryDatesDisplay.setText("Tanggal Trip Keseluruhan: " + startDateFormatted + " s/d " + endDateFormatted);

        // Update Destinations list title
        lblSummaryDestinationsDisplay.setText("Destinasi Terpilih:");
    }

    private void updateSummaryDisplays() {
        populateSummaryDisplay(); // Panggil metode yang sebenarnya melakukan update
    }


    private void updateEstimatedCost() {
        double currentCost = initialEstimatedCost;

        for (int i = 0; i < itineraryTable.getRowCount(); i++) {
            Object durationObj = itineraryTable.getValueAt(i, 3);
            if (durationObj instanceof Integer) {
                currentCost += ((Integer) durationObj) * 50000;
            }
        }
        
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));
    }

    private void updateNextStepButtonState() {
        boolean allDatesSet = true;
        for (int i = 0; i < itineraryTable.getRowCount(); i++) {
            if (itineraryTable.getValueAt(i, 2) == null) {
                allDatesSet = false;
                break;
            }
        }
        btnNextStep.setEnabled(itineraryTable.getRowCount() > 0 && allDatesSet);
    }

    private void updateMoveButtonsState() {
        int selectedRow = itineraryTable.getSelectedRow();
        btnMoveUp.setEnabled(selectedRow > 0);
        btnMoveDown.setEnabled(selectedRow != -1 && selectedRow < itineraryTableModel.getRowCount() - 1);
    }

    private void moveTableRow(int direction) {
        int selectedRow = itineraryTable.getSelectedRow();
        if (selectedRow != -1) {
            int newRow = selectedRow + direction;
            if (newRow >= 0 && newRow < itineraryTableModel.getRowCount()) {
                Object[] selectedRowData = new Object[itineraryTableModel.getColumnCount()];
                Object[] newRowData = new Object[itineraryTableModel.getColumnCount()];

                for (int i = 0; i < itineraryTableModel.getColumnCount(); i++) {
                    selectedRowData[i] = itineraryTableModel.getValueAt(selectedRow, i);
                    newRowData[i] = itineraryTableModel.getValueAt(newRow, i);
                }

                selectedRowData[0] = newRow + 1;
                newRowData[0] = selectedRow + 1;

                itineraryTableModel.removeRow(selectedRow);
                itineraryTableModel.insertRow(newRow, selectedRowData);

                itineraryTable.setRowSelectionInterval(newRow, newRow);
                updateSummaryDisplays();
                updateMoveButtonsState();
            }
        }
    }


    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_DESTINATION_STEP);
        } else {
            System.err.println("MainAppFrame reference is null in PanelItineraryStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        if (itineraryTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal satu destinasi dan atur tanggal untuk melanjutkan.", "Trip Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> destinationsForNextStep = new ArrayList<>();
        List<CustomTripDetailModel> itineraryDetailsForNextStep = new ArrayList<>();
        double currentTotalEstimatedCost = 0.0;

        for (int i = 0; i < itineraryTableModel.getRowCount(); i++) {
            String destName = (String) itineraryTableModel.getValueAt(i, 1);
            Object visitDateObj = itineraryTableModel.getValueAt(i, 2);
            Integer durasi = (Integer) itineraryTable.getValueAt(i, 3);

            LocalDate visitDate = null;
            if (visitDateObj instanceof Date) {
                visitDate = ((Date) visitDateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (visitDateObj instanceof LocalDate) {
                visitDate = (LocalDate) visitDateObj;
            }

            if (visitDate == null) {
                JOptionPane.showMessageDialog(this, "Tanggal kunjungan untuk '" + destName + "' belum diatur. Harap lengkapi semua tanggal.", "Validasi Tanggal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DestinasiModel dest = destinasiController.getDestinasiByName(destName); // Call getDestinasiByName
            if (dest == null) {
                JOptionPane.showMessageDialog(this, "Destinasi '" + destName + "' tidak ditemukan di database. Tidak dapat melanjutkan.", "Error Data", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CustomTripDetailModel detail = new CustomTripDetailModel();
            detail.setDestinasiId(dest.getId());
            detail.setTanggalKunjungan(visitDate);
            detail.setDurasiJam(durasi != null ? durasi : 0);
            detail.setUrutanKunjungan((Integer) itineraryTableModel.getValueAt(i, 0));
            detail.setHargaDestinasi(dest.getHarga());
            detail.setBiayaTransport(0.0);
            
            itineraryDetailsForNextStep.add(detail);
            destinationsForNextStep.add(destName);
        }
        
        try {
            String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
            currentTotalEstimatedCost = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
        } catch (ParseException e) {
            System.err.println("Error parsing estimated cost from label in ItineraryStep: " + e.getMessage());
            currentTotalEstimatedCost = initialEstimatedCost;
        }

        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRANSPORT_COST_STEP, 
                                   destinationsForNextStep, // List of destination names
                                   itineraryDetailsForNextStep, // List of CustomTripDetailModel
                                   currentTotalEstimatedCost); // Cumulative estimated cost
        } else {
            System.err.println("MainAppFrame reference is null in PanelItineraryStep (Next).");
        }
    }

    // --- Inner class for JDateChooser Table Cell Editor ---
    class DateChooserEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JDateChooser dateChooser;
        private Date currentDate;

        public DateChooserEditor() {
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("yyyy-MM-dd");
            dateChooser.setBorder(BorderFactory.createEmptyBorder());
            dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
                if ("date".equals(evt.getPropertyName())) {
                    currentDate = dateChooser.getDate();
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentDate = (Date) value;
            dateChooser.setDate(currentDate);
            return dateChooser;
        }

        @Override
        public Object getCellEditorValue() {
            return dateChooser.getDate();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    }

    // --- Inner class for Date Table Cell Renderer ---
    class DateChooserRenderer extends JLabel implements TableCellRenderer {
        public DateChooserRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                setText(new SimpleDateFormat("yyyy-MM-dd").format((Date) value));
            } else if (value instanceof LocalDate) {
                setText(((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                setText("");
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            setFont(table.getFont());
            return this;
        }
    }
}
