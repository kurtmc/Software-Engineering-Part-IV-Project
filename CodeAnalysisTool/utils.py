import subprocess

def run_command(cmd_list):
    return subprocess.Popen(cmd_list, stdout=subprocess.PIPE).communicate()[0].decode("utf-8")
