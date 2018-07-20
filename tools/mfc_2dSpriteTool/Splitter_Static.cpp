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
	// TODO: 여기에 명령 처리기 코드를 추가합니다.
	char name_filter[] = "All Files (*.*)|*.*|Image Files (*.png)|*.png||";

	// TRUE -> 열기대화상자, "cpp" -> 사용자가 확장자 없이 파일명만 입력했을때 자동으로 추가될 확장자명이다.
	// 즉, stdafx 까지만 입력하면 stdafx.cpp라고 입력한것과 동일하게 하고 싶을때 사용한다.
	// "*.cpp" 파일이름 에디트에 출력될 기본 문자열이다. 
	// OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT -> 파일 대화상자에 추가적으로 사용할 속성이다.
	// name_filter -> 파일 형식 콤보박스에 등록할 필터정보를 담고있는 메모리의 주소이다.
	CFileDialog ins_dlg(TRUE, "png", "*.*",
		OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT | OFN_NOCHANGEDIR, name_filter, NULL);

	// 파일 형식 콤보박스에 나열된 필터들 중에서 2번째 항목(*.cpp)을 선택한다.
	ins_dlg.m_ofn.nFilterIndex = 2;

	if (ins_dlg.DoModal() == IDOK){
		CMainFrame *pMain = (CMainFrame *)AfxGetMainWnd(); //App -> MainFrm
		CString file_title = ins_dlg.GetFileExt();
		if (file_title.Compare("dat") == 0){
			CSplitter_StaticDoc *pDoc = (CSplitter_StaticDoc *)pMain->GetActiveDocument();
			pDoc->SaveData(ins_dlg.GetPathName());
		}
		else{
			// 선택된 파일의 경로명을 이용하여 도큐먼트 정보를 재구성한다.
			
			//CSplitter_StaticDoc *pDoc = (CSplitter_StaticDoc *)pMain->GetActiveDocument();
			//pDoc->path_file = ins_dlg.GetPathName();
			CSplitter_StaticView *pView = (CSplitter_StaticView *)pMain->GetActiveView();
			pView->LoadImageFile(ins_dlg.GetPathName(), ins_dlg.GetFileTitle());
		}
	}
	else {
		// 원래 "파일 열기" 기능에서는 파일 열기를 취소했을 때, 특별한 메시지가 나오지 않지만
		// 재정의한 효과를 나타내기 위해서 파일 열기를 취소했을때, 취소했다는 메시지가 나오도록
		// 재구성 했습니다.
		::MessageBox(NULL, "파일 열기를 취소하였습니다.", "알림", MB_ICONINFORMATION);
	}
}