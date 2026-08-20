# UI Test Plan

These tests exercise Quackie's interactive command-line behavior. Each test case starts a fresh process and sends the commands in its `Inputs` block in order.

- Compile command: `javac -d /tmp/ui-test-classes src/main/java/*.java`
- Program command: `java -cp /tmp/ui-test-classes Quackie`

## Test Case 1: Add a task and list its status

- Aim: Verify that ordinary text is stored as a not-done task and displayed by `list`.
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
 Here are the tasks in your list:
 1.[ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```

## Test Case 2: Mark a task as done

- Aim: Verify that `mark N` changes the selected task to done and that `list` shows `[X]`.
- Inputs:
  ```text
  read book
  return book
  mark 2
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
 added: return book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [X] return book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] read book
 2.[X] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```

## Test Case 3: Reverse a task's done status

- Aim: Verify that `unmark N` changes a completed task back to not done.
- Inputs:
  ```text
  read book
  return book
  mark 2
  unmark 2
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
 added: return book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [X] return book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [ ] return book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] read book
 2.[ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```
