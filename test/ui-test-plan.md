# UI Test Plan

These tests exercise Quackie's interactive command-line behavior. Each test case starts a fresh process and sends the commands in its `Inputs` block in order.

- Compile command: `javac -d /tmp/ui-test-classes src/main/java/*.java`
- Program command: `java -cp /tmp/ui-test-classes Quackie`

## Test Case 1: Add a ToDo and list it

- Aim: Verify that `todo` creates a ToDo task and that `list` displays its type and not-done status.
- Inputs:
  ```text
  todo read book
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
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```

## Test Case 2: Mark a ToDo as done

- Aim: Verify that `mark N` changes a ToDo's status to done and that `list` shows `[X]`.
- Inputs:
  ```text
  todo read book
  todo return book
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
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] return book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] return book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[T][X] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```

## Test Case 3: Reverse a ToDo's done status

- Aim: Verify that `unmark N` changes a completed ToDo back to not done.
- Inputs:
  ```text
  todo read book
  todo return book
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
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] return book
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] return book
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] return book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[T][ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
  ```
