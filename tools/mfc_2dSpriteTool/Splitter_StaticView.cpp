// Splitter_StaticView.cpp : implementation of the CSplitter_StaticView class
//

#include "stdafx.h"
#include "Splitter_Static.h"

#include "Splitter_StaticDoc.h"
#include "Splitter_StaticView.h"
#include "MainFrm.h"
#include "RightView.h"
#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView

IMPLEMENT_DYNCREATE(CSplitter_StaticView, CScrollView)

BEGIN_MESSAGE_MAP(CSplitter_StaticView, CScrollView)
	//{{AFX_MSG_MAP(CSplitter_StaticView)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, CView::OnFilePrintPreview)
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView construction/destruction

CSplitter_StaticView::CSplitter_StaticView()
{
	// TODO: add construction code here
	IsImageLoading = false;
	IsDrawing = false;
	AniNumber = 0;
	SpriteNumber = 0;
}

CSplitter_StaticView::~CSplitter_StaticView()
{
}

BOOL CSplitter_StaticView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView drawing
void CSplitter_StaticView::LoadImageFile(CString path, CString name){
	if (IsImageLoading)
		Image.Destroy();

	Image.Load(path);
	IsImageLoading = true;

	CSplitter_StaticDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	pDoc->SetpreImage(path, name);

	AfxGetMainWnd()->Invalidate(FALSE);
	OnInitialUpdate();
}
void CSplitter_StaticView::OnDraw(CDC* pDC)
{
	// TODO: add draw code for native data here

	CRect rect;
	//if (!IsImageLoading)
	//else rect.SetRect(0, 0, Image.GetWidth(), Image.GetHeight());
	GetClientCurrentRect(rect);
	
	// 메모리 DC 선언
	CDC memDC;
	CBitmap *pOldBitmap, bitmap;
	memDC.CreateCompatibleDC(pDC);
	bitmap.CreateCompatibleBitmap(pDC, rect.Width(), rect.Height());

	pOldBitmap = memDC.SelectObject(&bitmap);
	memDC.PatBlt(0, 0, rect.Width(), rect.Height(), WHITENESS); // 흰색으로 초기화

	// 메모리 DC에 그리기
	DrawImage(&memDC);
	pDC->BitBlt(0, 0, rect.Width(), rect.Height(), &memDC, 0, 0, SRCCOPY);

	memDC.SelectObject(pOldBitmap);
	memDC.DeleteDC();
	bitmap.DeleteObject();
	
}
void CSplitter_StaticView::DrawImage(CDC *pDC)
{
	CSplitter_StaticDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	// 그리기 동작
	
	// 이미지 그리기
	if (IsImageLoading){
		//Image.BitBlt(pDC->m_hDC, 0, 0);
		Image.Draw(pDC->m_hDC, 0, 0);
	}
	// 사각형 그리기
//	for (int i = 0; i < 10; i++)
	int i = pDoc->AniNumber;
	for (int j = 0; j < pDoc->saveRectIndex[i]; j++){
		CRect rect;
		rect.CopyRect(&(pDoc->saveRect[i][j]));
		CBrush brush;
		brush.CreateStockObject(NULL_BRUSH);
		CBrush *pOldBrush = pDC->SelectObject(&brush);
		CPen pen;
		pen.CreatePen(PS_SOLID, 1, RGB(255, 0, 0));    // 빨간색 펜을 생성
		CPen* oldPen = pDC->SelectObject(&pen);
		pDC->Rectangle(rect.left, rect.top, rect.right, rect.bottom);
		pDC->SelectObject(oldPen);     // 시스템 펜 객체를 돌려줌
		pDC->SelectObject(pOldBrush);
	}
	// 선그리기
	{
		/* 보간 rect*/
		CRect rc;
		GetClientCurrentRect(rc);
		CPen test_dot_tick1_pen(PS_DOT, 1, RGB(0, 0, 0));
		CPen *p_old_pen = pDC->SelectObject(&test_dot_tick1_pen);
		pDC->MoveTo(rc.left, currentPoint.y); pDC->LineTo(rc.right, currentPoint.y);
		pDC->MoveTo(currentPoint.x, rc.top); pDC->LineTo(currentPoint.x, rc.bottom);

		pDC->SelectObject(p_old_pen);

		if (IsDrawing)
		{
			CBrush brush;
			brush.CreateStockObject(NULL_BRUSH);
			CBrush *pOldBrush = pDC->SelectObject(&brush);
			CPen pen;
			pen.CreatePen(PS_SOLID, 1, RGB(0, 0, 255));    // 빨간색 펜을 생성
			CPen* oldPen = pDC->SelectObject(&pen);
			pDC->Rectangle(prePoint.x, prePoint.y, currentPoint.x, currentPoint.y);
			pDC->SelectObject(oldPen);     // 시스템 펜 객체를 돌려줌
			pDC->SelectObject(pOldBrush);
		}
	}
}
/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView printing
void CSplitter_StaticView::GetClientCurrentRect(CRect &rect){
	GetClientRect(&rect);
	SCROLLINFO  SP;
	GetScrollInfo(SB_HORZ, &SP);
	rect.right += SP.nPos;
	GetScrollInfo(SB_VERT, &SP);
	rect.bottom += SP.nPos;
}
void CSplitter_StaticView::GetClientCurrentPoint(CPoint &pos){
	SCROLLINFO  SP;
	CPoint cur_pos;
	GetScrollInfo(SB_HORZ, &SP);
	pos.x += SP.nPos;
	GetScrollInfo(SB_VERT, &SP);
	pos.y += SP.nPos;
}
BOOL CSplitter_StaticView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void CSplitter_StaticView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void CSplitter_StaticView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView diagnostics

#ifdef _DEBUG
void CSplitter_StaticView::AssertValid() const
{
	CView::AssertValid();
}

void CSplitter_StaticView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CSplitter_StaticDoc* CSplitter_StaticView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CSplitter_StaticDoc)));
	return (CSplitter_StaticDoc*)m_pDocument;
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticView message handlers


void CSplitter_StaticView::OnInitialUpdate()
{
	CView::OnInitialUpdate();

	// TODO: Add your specialized code here and/or call the base class
	
	if (IsImageLoading){
		CSize sizeViewPage;
		sizeViewPage.cx = Image.GetWidth();
		sizeViewPage.cy = Image.GetHeight();

		CSize sizeViewPageTotal;
		sizeViewPageTotal.cx = sizeViewPage.cx;
		sizeViewPageTotal.cy = sizeViewPage.cy;

		SetScrollSizes(MM_TEXT, sizeViewPageTotal);
	}
	else{
		CRect rc;
		GetClientRect(&rc);
		CSize sizeViewPage;
		sizeViewPage.cx = rc.right - rc.left;
		sizeViewPage.cy = rc.bottom - rc.top;

		CSize sizeViewPageTotal;
		sizeViewPageTotal.cx = sizeViewPage.cx;
		sizeViewPageTotal.cy = sizeViewPage.cy;

		SetScrollSizes(MM_TEXT, sizeViewPageTotal);
	}
}
void CSplitter_StaticView::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	currentPoint = point;
	GetClientCurrentPoint(currentPoint);

	//currentPoint = point;

	CSplitter_StaticDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	pDoc->UpdateMousePOS(currentPoint);
	pDoc->UpdateAllViews(this);
	Invalidate();
	CScrollView::OnMouseMove(nFlags, point);
}
void CSplitter_StaticView::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	CRect rc;
	GetClientRect(&rc);

	if (point.x >= rc.left &&
		point.x <= rc.right &&
		point.y >= rc.top &&
		point.y <= rc.bottom){
		prePoint = point;
		GetClientCurrentPoint(prePoint);

		IsDrawing = true;
	}
	CScrollView::OnLButtonDown(nFlags, point);
}
void CSplitter_StaticView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	CPoint cur_pos = point;
	GetClientCurrentPoint(cur_pos);

	if (IsDrawing &&
		cur_pos.x > prePoint.x &&
		cur_pos.y > prePoint.y &&
		// 에러처리 : 이미지 바운드
		(cur_pos.x <= Image.GetWidth() && cur_pos.y <= Image.GetHeight())){
		// size bigger then 0
		// draw rect
		
		CSplitter_StaticDoc* pDoc = GetDocument();
		ASSERT_VALID(pDoc);
		//draw success, save rectInfo
		
		CRect rect = { prePoint.x, prePoint.y, cur_pos.x, cur_pos.y };

		CRect modify_rect = {9999,9999,-9999,-9999};
		/* 영역을 잘 잡아주는 부분 추가 */
		/* 마우스로 잡은 영역 & 이미지 영역을 alpha 검사해서 round를 잡아준다 */
		for (int i = prePoint.y; i <= cur_pos.y; i++)
		for (int j = prePoint.x; j <= cur_pos.x; j++){
			unsigned char * pCol = (unsigned char *)Image.GetPixelAddress(j, i);
			unsigned char alpha = pCol[3];
			if (alpha > 0){
				if (modify_rect.left > j) modify_rect.left = j;
				if (modify_rect.top > i) modify_rect.top = i;
				if (modify_rect.right < j) modify_rect.right = j;
				if (modify_rect.bottom < i) modify_rect.bottom = i;		
			}
			/*
			//|| ((255 - pCol[0]) + (255 - pCol[1]) + (255 - pCol[2]) > 0)
			COLORREF corBuff = Image.GetPixel(i, j);
			int rValue = GetRValue(corBuff);
			int gValue = GetGValue(corBuff);
			int bValue = GetBValue(corBuff);
			*/
		}
		//--modify_rect.left; --modify_rect.top;
		++modify_rect.right; ++modify_rect.bottom;
		pDoc->UpdateRect(modify_rect);

		Invalidate();
	}
	IsDrawing = false;

	CScrollView::OnLButtonUp(nFlags, point);
}
BOOL CSplitter_StaticView::OnEraseBkgnd(CDC* pDC)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	
	//return CScrollView::OnEraseBkgnd(pDC);
	/* 깜빡임 방지 */
	return FALSE;
}
