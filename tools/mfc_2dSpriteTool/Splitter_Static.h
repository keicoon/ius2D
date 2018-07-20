// Splitter_Static.h : main header file for the SPLITTER_STATIC application
//

#if !defined(AFX_SPLITTER_STATIC_H__0532925A_2526_4394_9F6C_86C4FEB1C81E__INCLUDED_)
#define AFX_SPLITTER_STATIC_H__0532925A_2526_4394_9F6C_86C4FEB1C81E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticApp:
// See Splitter_Static.cpp for the implementation of this class
//

class CSplitter_StaticApp : public CWinApp
{
public:
	CSplitter_StaticApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CSplitter_StaticApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation
	//{{AFX_MSG(CSplitter_StaticApp)
	afx_msg void OnAppAbout();
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
	afx_msg void OnFileOpen();
//	afx_msg void OnFileSaveAs();
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SPLITTER_STATIC_H__0532925A_2526_4394_9F6C_86C4FEB1C81E__INCLUDED_)
