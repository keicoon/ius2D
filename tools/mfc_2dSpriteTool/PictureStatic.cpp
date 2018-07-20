// PictureStatic.cpp : ���� �����Դϴ�.
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



// CPictureStatic �޽��� ó�����Դϴ�.




void CPictureStatic::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: ���⿡ �޽��� ó���� �ڵ带 �߰��մϴ�.
	// �׸��� �޽����� ���ؼ��� CStatic::OnPaint()��(��) ȣ������ ���ʽÿ�.
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
	// TODO: ���⿡ �޽��� ó���� �ڵ带 �߰� ��/�Ǵ� �⺻���� ȣ���մϴ�.
	return FALSE;
	//return CStatic::OnEraseBkgnd(pDC);
}