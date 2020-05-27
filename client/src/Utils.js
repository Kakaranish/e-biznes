import axios from 'axios';

export const isValidUUID = value =>
    /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(value);

export const getFormDataJsonFromEvent = event => {
    const formData = new FormData(event.target);

    let formDataJson = {};
    for (const [key, value] of formData.entries()) {
        formDataJson[key] = value;
    }

    return formDataJson;
}


export const doRequest = async requestAction => {
    const result = await requestAction();
    if(result.status === 200) return result.data;
    
    console.log(result);
    throw result.status;
}