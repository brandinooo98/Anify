const SongEmbed = (props) => {
  const apiResults = props.apiResults.songEmbeds;
  return (
    <div>
      {apiResults &&
        apiResults.map((result) => (
          <div
            className="center"
            dangerouslySetInnerHTML={{ __html: result }}
          />
        ))}
    </div>
  );
};
export default SongEmbed;
