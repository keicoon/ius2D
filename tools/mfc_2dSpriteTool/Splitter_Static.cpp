// Splitter_Static.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "Splitter_Static.h"

#include "MainFrm.h"
#include "Splitter_StaticDoc.h"
#include "Splitter_StaticView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticApp

BEGIN_MESSAGE_MAP(CSplitter_StaticApp, CWinApp)
	//{{AFX_MSG_MAP(CSplitter_StaticApp)
	ON_COMMAND(ID_APP_ABOUT, OnAppAbout)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
	// Standard file based document commands
	ON_COMMAND(ID_FILE_NEW, CWinApp::OnFileNew)
//	ON_COMMAND(ID_FILE_OPEN, CWinApp::OnFileOpen)
	// Standard print setup command
	ON_COMMAND(ID_FILE_PRINT_SETUP, CWinApp::OnFilePrintSetup)
	ON_COMMAND(ID_FILE_OPEN, &CSplitter_StaticApp::OnFileOpen)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticApp construction

CSplitter_StaticApp::CSplitter_StaticApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CSplitter_StaticApp object

CSplitter_StaticApp theApp;

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticApp initialization

BOOL CSplitter_StaticApp::InitInstance()
{
	// Standard initialization
	// If you are not using these features and wish to reduce the size
	//  of your final executable, you should remove from the following
	//  the specific initialization routines you do not need.

#ifdef _AFXDLL
	Enable3dControls();			// Call this when using MFC in a shared DLL
#else
	Enable3dControlsStatic();	// Call this when linking to MFC statically
#endif

	// Change the registry key under which our settings are stored.
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization.
	SetRegistryKey(_T("Local AppWizard-Generated Applications"));

	LoadStdProfileSettings();  // Load standard INI file options (including MRU)

	// Register the application's document templates.  Document templates
	//  serve as the connection between documents, frame windows and views.

	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CSplitter_StaticDoc),
		RUNTIME_CLASS(CMainFrame),       // main SDI frame window
		RUNTIME_CLASS(CSplitter_StaticView));
	AddDocTemplate(pDocTemplate);

	// Parse command line for standard shell commands, DDE, file open
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	// Dispatch commands specified on the command line
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	// The one and only window has been initialized, so show and update it.
	m_pMainWnd->ShowWindow(SW_SHOWMAXIMIZED);
	m_pMainWnd->UpdateWindow();

	return TRUE;
}


/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
		// No message handlers
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

// App command to run the dialog
void CSplitter_StaticApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticApp message handlers



void CSplitter_StaticApp::OnFileOpen()
{
	// TODO: ���⿡ ��� ó���� �ڵ带 �߰��մϴ�.
	char name_filter[] = "All Files (*.*)|*.*|Image Files (*.png)|*.png||";

	// TRUE -> �����ȭ����, "cpp" -> ����ڰ� Ȯ���� ���� ���ϸ� �Է������� �ڵ����� �߰��� Ȯ���ڸ��̴�.
	// ��, stdafx ������ �Է��ϸ� stdafx.cpp��� �Է��ѰͰ� �����ϰ� �ϰ� ������ ����Ѵ�.
	// "*.cpp" �����̸� ����Ʈ�� ��µ� �⺻ ���ڿ��̴�. 
	// OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT -> ���� ��ȭ���ڿ� �߰������� ����� �Ӽ��̴�.
	// name_filter -> ���� ���� �޺��ڽ��� ����� ���������� ����ִ� �޸��� �ּ��̴�.
	CFileDialog ins_dlg(TRUE, "png", "*.*",
		OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT | OFN_NOCHANGEDIR, name_filter, NULL);

	// ���� ���� �޺��ڽ��� ������ ���͵� �߿��� 2��° �׸�(*.cpp)�� �����Ѵ�.
	ins_dlg.m_ofn.nFilterIndex = 2;

	if (ins_dlg.DoModal() == IDOK){
		CMainFrame *pMain = (CMainFrame *)AfxGetMainWnd(); //App -> MainFrm
		CString file_title = ins_dlg.GetFileExt();
		if (file_title.Compare("dat") == 0){
			CSplitter_StaticDoc *pDoc = (CSplitter_StaticDoc *)pMain->GetActiveDocument();
			pDoc->SaveData(ins_dlg.GetPathName());
		}
		else{
			// ���õ� ������ ��θ��� �̿��Ͽ� ��ť��Ʈ ������ �籸���Ѵ�.
			
			//CSplitter_StaticDoc *pDoc = (CSplitter_StaticDoc *)pMain->GetActiveDocument();
			//pDoc->path_file = ins_dlg.GetPathName();
			CSplitter_StaticView *pView = (CSplitter_StaticView *)pMain->GetActiveView();
			pView->LoadImageFile(ins_dlg.GetPathName(), ins_dlg.GetFileTitle());
		}
	}
	else {
		// ���� "���� ����" ��ɿ����� ���� ���⸦ ������� ��, Ư���� �޽����� ������ ������
		// �������� ȿ���� ��Ÿ���� ���ؼ� ���� ���⸦ ���������, ����ߴٴ� �޽����� ��������
		// �籸�� �߽��ϴ�.
		::MessageBox(NULL, "���� ���⸦ ����Ͽ����ϴ�.", "�˸�", MB_ICONINFORMATION);
	}
}