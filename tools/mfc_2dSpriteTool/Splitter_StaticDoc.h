// Splitter_StaticDoc.h : interface of the CSplitter_StaticDoc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_SPLITTER_STATICDOC_H__52E84840_8998_47D1_95FD_AA68BE6939F7__INCLUDED_)
#define AFX_SPLITTER_STATICDOC_H__52E84840_8998_47D1_95FD_AA68BE6939F7__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define MAX_ANIMATION_NUM 20
#define MAX_SPRITE_NUM 20

class CSplitter_StaticDoc : public CDocument
{
protected: // create from serialization only
	CSplitter_StaticDoc();
	DECLARE_DYNCREATE(CSplitter_StaticDoc)

// Attributes
public:
	CString		name_file;
	CString		path_file;
	CPoint		mousePOS;
	CRect		saveRect[MAX_ANIMATION_NUM][MAX_SPRITE_NUM];
	int			saveRectIndex[MAX_ANIMATION_NUM];

	int			AniNumber;
	int			SpriteNumber;

	CImage		m_drawImage;
	bool		IsLoadingImage;
// Operations
public:
	void SaveData(CString path);
	void SetpreImage(CString path, CString name);
	void UpdateMousePOS(CPoint pos);
	void UpdateRect(CRect rect);
// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CSplitter_StaticDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CSplitter_StaticDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CSplitter_StaticDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnFileSave();
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SPLITTER_STATICDOC_H__52E84840_8998_47D1_95FD_AA68BE6939F7__INCLUDED_)
