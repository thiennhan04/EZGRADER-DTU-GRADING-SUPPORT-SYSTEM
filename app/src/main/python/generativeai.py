import google.generativeai as palm


def grader(question, correct_answer, student_answer):
    API_KEY = 'AIzaSyBFy5yH5ajJN1hLeBBkwnkNvu1pacJVWdg'
    palm.configure(api_key=API_KEY)

    # model_list = [_ for _ in palm.list_models()]
    # for model in model_list:
    #     print(model.name)

    question = 'What do you go to school?' + '\n'
    correct_answer = 'I going to school by bike' + '\n'
    student_answer = 'Igoing to School by bike' + '\n'

    model_id = 'models/text-bison-001'
    prompt = ('From the question: ' + question +
              'Correct answer: ' + correct_answer +
              'Student answer: ' + student_answer + ('Students answer yes or no (only answer '
                                                     'yes or no)'))

    completion = palm.generate_text(
        model=model_id,
        prompt=prompt,
        temperature=0.99,
        max_output_tokens=20,
        candidate_count=2
    )
    # print(prompt)
    return completion.result
# print(grader())