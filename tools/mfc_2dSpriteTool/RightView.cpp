// RightView.cpp : implementation file
//

#include "stdafx.h"
#include "Splitter_Static.h"
#include "RightView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CRightView

IMPLEMENT_DYNCREATE(CRightView, CFormView)

CRightView::CRightView()
	: CFormView(CRightView::IDD)
{
	//{{AFX_DATA_INIT(CRightView)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}

CRightView::~CRightView()
{
}

void CRightView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CRightView)
	// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
	DDX_Control(pDX, IDC_LIST1, m_list);
	DDX_Control(pDX, IDC_PIMAGE, preImage);
}

BEGIN_MESSAGE_MAP(CRightView, CFormView)
	//{{AFX_MSG_MAP(CRightView)
		// NOTE - the ClassWizard will add and remove mapping macros here.
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_BUTTON1, &CRightView::OnBnClickedButton1)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CRightView diagnostics

#ifdef _DEBUG
void CRightView::AssertValid() const
{
	CFormView::AssertValid();
}

void CRightView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CRightView message handlers


void CRightView::OnDraw(CDC* /*pDC*/)
{
	// TODO: 여기에 특수화된 코드를 추가 및/또는 기본 클래스를 호출합니다.
	CSplitter_StaticDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);

	CString str;
	
	int AniN = pDoc->AniNumber;
	//int SpriteN = pDoc->SpriteNumber;
	/* rect List 정보 출력 */
	m_list.SetCurSel(m_list.GetSelCount());
	m_list.ResetContent();
	for (int i = 0; i < pDoc->saveRectIndex[AniN]; i++)
	{
		CRect rect;
		rect.CopyRect(&(pDoc->saveRect[AniN][i]));
		str.Format("%d: %d %d %d %d", i, rect.left, rect.top, rect.right, rect.bottom);
		m_list.AddString(str);
	}
	UpdateData();

	/* 마우스 좌표를 Update한다 */
	str.Format("%d", pDoc->mousePOS.x);
	SetDlgItemText(IDC_MX, str);
	str.Format("%d", pDoc->mousePOS.y);
	SetDlgItemText(IDC_MY, str);
	str.Format("%d", pDoc->AniNumber);
	SetDlgItemText(IDC_AN, str);
	str.Format("%d", pDoc->SpriteNumber);
	SetDlgItemText(IDC_SN, str);

	preImage.Invalidate();
}

CSplitter_StaticDoc* CRightView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSplitter_StaticDoc)));
	return (CSplitter_StaticDoc*)m_pDocument;
}
void CRightView::OnBnClickedButton1()
{
	// TODO: 여기에 컨트롤 알림 처리기 코드를 추가합니다.
	
	CSplitter_StaticDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);

	int AniN = GetDlgItemInt(IDC_EDIT1);
	int SpriteN = GetDlgItemInt(IDC_EDIT2);

	pDoc->AniNumber = AniN;
	pDoc->SpriteNumber = SpriteN;

	pDoc->UpdateAllViews(NULL);
}
