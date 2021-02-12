# Configuration script for StockApp
# Run this script to set email adress and password for sending email notifications


def replace_string(filename, old_string, new_string):
    lines = None
    with open(filename, 'r') as reader:
        lines = reader.readlines()
        for num, line in enumerate(lines):
            if line.find(old_string):
                lines[num] = line.replace(old_string, new_string)

    if lines is not None:
        with open(filename, 'w') as writer:
            writer.writelines(lines)


def main():
    print('This is configuration script for StockApp')
    print('You will be asked to type in email box information')
    while True:
        answer = input('Do you want to continue? y/n ')
        if answer == 'n':
            return
        elif answer == 'y':
            break

    new_adress = input("Type new sending email adress: ")
    new_password = input("Type password associated with email adress: ")
    replace_string('projekt/src/pl/edu/pw/mini/zpoif/projekt/jung_sawicki/notifications/MailSender.java',
                   'private@mail.adress.com', new_adress)
    replace_string('projekt/src/pl/edu/pw/mini/zpoif/projekt/jung_sawicki/notifications/MailSender.java', '"password"',
                   '"' + new_password + '"')


if __name__ == '__main__':
    main()
