import React from 'react';
import { useHistory } from 'react-router-dom';
import Opinion from './Opinion';
import EditableOpinion from './EditableOpinion';
import AddOpinionForm from './AddOpinionForm';

const Opinions = (props) => {

    const state = props.state;
    const history = useHistory();
    
    return <>
        <h3 className="mb-4">Opinions</h3>
        {
            !state.result.opinions || state.result.opinions.length === 0
                ? <p>No opinions yet</p>
                : <>
                    {
                        state.result.opinions.map(o =>
                            o.user.id !== state.result.userId
                                ? <Opinion opinionInfo={o} key={`op-${o.opinion.id}`} />
                                : <EditableOpinion opinionInfo={o} key={`op-${o.opinion.id}`} />
                        )
                    }
                </>
        }

        {
            state.result.allowAddOpinion &&
            <AddOpinionForm productId={state.result.product.id} callback={() => history.go()} />
        }

    </>
};

export default Opinions;