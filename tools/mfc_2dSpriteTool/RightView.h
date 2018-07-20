#if !defined(AFX_RIGHTVIEW_H__40DD9F67_1463_4E7D_9CC6_321B0F9F90E1__INCLUDED_)
#define AFX_RIGHTVIEW_H__40DD9F67_1463_4E7D_9CC6_321B0F9F90E1__INCLUDED_
#include "Splitter_StaticDoc.h"
#include "PictureStatic.h"
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// RightView.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CRightView form view

#ifndef __AFXEXT_H__
#include <afxext.h>
#endif

class CRightView : public CFormView
{
protected:
	CRightView();           // protected constructor used by dynamic creation
	DECLARE_DYNCREATE(CRightView)

// Form Data
public:
	//{{AFX_DATA(CRightView)
	enum { IDD = IDD_DIALOG1 };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

// Attributes
public:

// Operations
public:
	CSplitter_StaticDoc* GetDocument();
// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CRightView)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	virtual ~CRightView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	// Generated message map functions
	//{{AFX_MSG(CRightView)
		// NOTE - the ClassWizard will add and remove member functions here.
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
	virtual void OnDraw(CDC* /*pDC*/);
public:
//	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	CListBox m_list;
	afx_msg void OnBnClickedButton1();
	CPictureStatic preImage;
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_RIGHTVIEW_H__40DD9F67_1463_4E7D_9CC6_321B0F9F90E1__INCLUDED_)
#include "afxwin.h"
