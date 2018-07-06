#pragma once

#include "StdAfx.h"
// CPictureStatic

class CPictureStatic : public CStatic
{
	DECLARE_DYNAMIC(CPictureStatic)
public:
	//void			SetImage(CString path);
public:
	CPictureStatic();
	virtual ~CPictureStatic();

protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnPaint();
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
};


