# UI Test Plan

These tests exercise Quackie's interactive command-line behavior. Each test case starts a fresh process and sends the commands in its `Inputs` block in order.

- Compile command: `javac -d /tmp/ui-test-classes src/main/java/*.java`
- Program command: `java -cp /tmp/ui-test-classes Quackie`

## Test Case 1: Add a task and list it

- Aim: Verify that ordinary text is stored as a new task and displayed by `list`.
- Inputs:
  ```text
  read book
  list
  bye
  ```
- Expected output:
  ```text
____________________________________________________________
                           _      _      
  __ _  _   _   __ _  ___| | __ (_)  ___ 
 / _` || | | | / _` |/ __| |/ / | | / _ \ 
| (_| || |_| || (_| | (__|   <  | ||  __/
 \__, | \__,_| \__,_|\___|_|\_\ |_|\___|
    |_|                                  
Hello! I'm Quackie.
What can I do for you?
____________________________________________________________
____________________________________________________________
 added: read book
____________________________________________________________
____________________________________________________________
1. read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```
