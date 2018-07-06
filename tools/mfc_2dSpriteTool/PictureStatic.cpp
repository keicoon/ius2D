// PictureStatic.cpp : 구현 파일입니다.
//

#include "stdafx.h"
#include "PictureStatic.h"
#include "MainFrm.h"
#include "Splitter_StaticDoc.h"
// CPictureStatic

IMPLEMENT_DYNAMIC(CPictureStatic, CStatic)

CPictureStatic::CPictureStatic()
{
	
}

CPictureStatic::~CPictureStatic()
{
}


BEGIN_MESSAGE_MAP(CPictureStatic, CStatic)
	ON_WM_PAINT()
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()



// CPictureStatic 메시지 처리기입니다.




void CPictureStatic::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 여기에 메시지 처리기 코드를 추가합니다.
	// 그리기 메시지에 대해서는 CStatic::OnPaint()을(를) 호출하지 마십시오.
	CRect rcClient;
	GetClientRect(&rcClient);

	dc.FillSolidRect(&rcClient, RGB(255, 255, 255));
	CMainFrame *pMain = (CMainFrame *)AfxGetMainWnd(); //App -> MainFrm
	CSplitter_StaticDoc *pDoc = (CSplitter_StaticDoc *)pMain->GetActiveDocument();
	if (!pDoc->m_drawImage.IsNull())
	{
		CRect rect = {0,0,0,0};
		
		int Ani = pDoc->AniNumber;
		int Sprite = pDoc->SpriteNumber;
		rect.CopyRect(&(pDoc->saveRect[Ani][Sprite]));
		if (rect.Width() > 0 && rect.Height() > 0){
			float fScale = min(rcClient.Width() / (float)rect.Width(), rcClient.Height() / (float)rect.Height());
			int c_left = (rcClient.Width() - rect.Width()*fScale) / 2;
			int c_right = (rcClient.Height() - rect.Height()*fScale) / 2;
			pDoc->m_drawImage.Draw(dc.m_hDC,
				c_left, c_right, rect.Width()*fScale, rect.Height()*fScale,
				rect.left, rect.top, rect.Width(), rect.Height());
		}
	}
}


BOOL CPictureStatic::OnEraseBkgnd(CDC* pDC)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	return FALSE;
	//return CStatic::OnEraseBkgnd(pDC);
}