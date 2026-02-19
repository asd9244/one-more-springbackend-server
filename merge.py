import os

# 1. 분석에서 제외할 폴더 및 확장자 설정
EXCLUDE_DIRS = {'.git', '.gradle', '.idea', 'build', 'bin', 'node_modules', 'gradle'}
INCLUDE_EXTENSIONS = {'.java', '.gradle', '.properties', '.env', '.md'}

# 2. 결과 파일 이름
OUTPUT_FILE = "project_context_summary.txt"

def summarize_project(root_path):
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as outfile:
        for root, dirs, files in os.walk(root_path):
            # 제외할 폴더 필터링
            dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]

            for file in files:
                if any(file.endswith(ext) for ext in INCLUDE_EXTENSIONS):
                    file_path = os.path.join(root, file)
                    relative_path = os.path.relpath(file_path, root_path)

                    try:
                        with open(file_path, 'r', encoding='utf-8') as infile:
                            content = infile.read()

                            # 파일 구분선 추가
                            outfile.write(f"\n{'='*80}\n")
                            outfile.write(f" FILE: {relative_path}\n")
                            outfile.write(f"{'='*80}\n\n")
                            outfile.write(content)
                            outfile.write("\n")

                            print(f"✅ 포함됨: {relative_path}")
                    except Exception as e:
                        print(f"❌ 읽기 실패: {relative_path} ({e})")

    print(f"\n🚀 모든 파일이 '{OUTPUT_FILE}'로 합쳐졌어!")

if __name__ == "__main__":
    # 현재 스크립트가 실행되는 위치를 기준으로 작업
    project_root = os.getcwd()
    summarize_project(project_root)