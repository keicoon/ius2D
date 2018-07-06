// Splitter_StaticDoc.cpp : implementation of the CSplitter_StaticDoc class
//

#include "stdafx.h"
#include "Splitter_Static.h"

#include "Splitter_StaticDoc.h"
#include "MainFrm.h"
#include "RightView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticDoc

IMPLEMENT_DYNCREATE(CSplitter_StaticDoc, CDocument)

BEGIN_MESSAGE_MAP(CSplitter_StaticDoc, CDocument)
	//{{AFX_MSG_MAP(CSplitter_StaticDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
	ON_COMMAND(ID_FILE_SAVE, &CSplitter_StaticDoc::OnFileSave)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticDoc construction/destruction

CSplitter_StaticDoc::CSplitter_StaticDoc()
{
	// TODO: add one-time construction code here
	path_file = "null";
	AniNumber = 0;
	SpriteNumber = 0;
	IsLoadingImage = false;
	for (int i = 0; i < MAX_ANIMATION_NUM; i++)
		saveRectIndex[i] = 0;
}

CSplitter_StaticDoc::~CSplitter_StaticDoc()
{
}
int GetFindCharCount(CString parm_string, char parm_find_char)
{
	int length = parm_string.GetLength(), find_count = 0;

	for (int i = 0; i < length; i++){
		if (parm_string[i] == parm_find_char) find_count++;
	}

	return find_count;
}
void CSplitter_StaticDoc::SaveData(CString path)
{
	CFile file;
	char* read;
	CString rStr;
	if (file.Open(path, CFile::modeRead))
	{
		UINT nBytes = file.GetLength();
		if (nBytes >= 0)
		{
			read = new char[nBytes + 1];
			file.Read(read, nBytes);
			read[nBytes] = 0;
			rStr.Empty();
			rStr = CString(read);
			delete read;
		}
		file.Close();
	}

	int sepCount = GetFindCharCount(rStr, '/');   // " , " 의 갯수를 세어온다.
	CString* temp1 = new CString[sepCount + 1]; // 구해온 갯수만큼 동적 배열을 할당(CString배열)
	CString strTok;
	int cnt = 0;
	while (AfxExtractSubString(strTok, rStr, cnt, '/'))  // 문자를 잘라준다. (AfxExtractSubString = strTok)
		temp1[cnt++] = strTok;                                       // 해당 배열에 순차적으로 넣어준다.

	//주의 temp1[0], temp1[1], temp1[2], temp1[3]
	int ani_num = _ttoi(temp1[3]);
	int INDEX = 4;
	int sprite_num;
	for (int i = 0; i < ani_num; i++){
		sprite_num = _ttoi(temp1[INDEX++]);
		for (int j = 0; j < sprite_num; j++)
		{
			++saveRectIndex[i];
			saveRect[i][j].SetRect(_ttoi(temp1[INDEX]), _ttoi(temp1[INDEX + 1]), _ttoi(temp1[INDEX + 2]), _ttoi(temp1[INDEX + 3]));
			INDEX += 4;
		}
		//++INDEX;
	}

	delete[] temp1;
}

void CSplitter_StaticDoc::SetpreImage(CString path, CString name)
{
	name_file = name;
	path_file = path;
	if (IsLoadingImage)
		m_drawImage.Destroy();
	m_drawImage.Load(path);
	IsLoadingImage = true;
}
void CSplitter_StaticDoc::UpdateMousePOS(CPoint pos)
{
	mousePOS = pos;
	//CMainFrame *pMain = (CMainFrame *)AfxGetMainWnd(); //Doc -> MainFrm
	//CRightView *pView = (CRightView *)pMain->GetActiveView();
	//pView->Invalidate();
}
void CSplitter_StaticDoc::UpdateRect(CRect rect)
{
	saveRect[AniNumber][SpriteNumber].CopyRect(&rect);

	if (!(SpriteNumber < saveRectIndex[AniNumber])){//refactoring
		++saveRectIndex[AniNumber];
		++SpriteNumber;
	}
}
BOOL CSplitter_StaticDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticDoc serialization

void CSplitter_StaticDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticDoc diagnostics

#ifdef _DEBUG
void CSplitter_StaticDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CSplitter_StaticDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CSplitter_StaticDoc commands


void CSplitter_StaticDoc::OnFileSave()
{
	// TODO: 여기에 명령 처리기 코드를 추가합니다.
	CString str;
	CFileDialog ins_dlg(FALSE, NULL, "*.dat", OFN_HIDEREADONLY | OFN_NOCHANGEDIR,
		"dat Files(*.dat)|*.dat||");
	if (ins_dlg.DoModal() == IDOK){
		FILE *p_file = fopen(ins_dlg.GetPathName(), "wt");
		if (!m_drawImage.IsNull() && p_file != NULL){
			/* 저장할 내용 작성*/
			// header
			fprintf(p_file, "%s/%d/%d/", name_file, m_drawImage.GetWidth(), m_drawImage.GetHeight());
			int total_aniN=0;
			for (int i = 0; i < MAX_ANIMATION_NUM;i++)
			if (saveRectIndex[i] != 0)++total_aniN;
			fprintf(p_file,"%d/", total_aniN);

			/* 주의 */
			for (int i = 0; i < total_aniN; i++){
				fprintf(p_file, "%d/", saveRectIndex[i]);
				for (int j = 0; j < saveRectIndex[i]; j++)
				{
					CRect rect;
					rect.CopyRect(&(saveRect[i][j]));
					fprintf(p_file, "%d/%d/%d/%d/", rect.left, rect.top, rect.right, rect.bottom);
				}
			}
			//fprintf(p_file,"",);
		}
		fclose(p_file);
	}
}
