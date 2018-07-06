// Splitter_StaticView.h : interface of the CSplitter_StaticView class
//
/////////////////////////////////////////////////////////////////////////////
#include "StdAfx.h"
#if !defined(AFX_SPLITTER_STATICVIEW_H__C2EB188F_FBE1_4A71_8AEE_7B43BC25DD89__INCLUDED_)
#define AFX_SPLITTER_STATICVIEW_H__C2EB188F_FBE1_4A71_8AEE_7B43BC25DD89__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CSplitter_StaticView : public CScrollView
{

protected: // create from serialization only
	CSplitter_StaticView();
	DECLARE_DYNCREATE(CSplitter_StaticView)

// Attributes
public:
	CImage	Image;
	bool	IsImageLoading;
	CPoint	prePoint;
	bool	IsDrawing;
	CPoint	currentPoint;
	CSize msizeViewPage;

	int AniNumber;
	int SpriteNumber;
	int saveRect_Index;
// Operations
public:
	CSplitter_StaticDoc* GetDocument();
	void LoadImageFile(CString path, CString name);
	void DrawImage(CDC *pDC);
	void GetClientCurrentRect(CRect &rect);
	void GetClientCurrentPoint(CPoint &pos);

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CSplitter_StaticView)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CSplitter_StaticView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CSplitter_StaticView)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
public:
	virtual void OnInitialUpdate();


	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
};

#ifndef _DEBUG  // debug version in Splitter_StaticView.cpp
inline CSplitter_StaticDoc* CSplitter_StaticView::GetDocument()
   { return (CSplitter_StaticDoc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SPLITTER_STATICVIEW_H__C2EB188F_FBE1_4A71_8AEE_7B43BC25DD89__INCLUDED_)
